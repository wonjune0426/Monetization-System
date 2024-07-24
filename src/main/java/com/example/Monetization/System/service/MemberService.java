package com.example.monetization.system.service;


import com.example.monetization.system.dto.VideoAmountDto;
import com.example.monetization.system.dto.request.member.SignupRequestDto;
import com.example.monetization.system.dto.response.CalculateResponseDto;
import com.example.monetization.system.dto.response.ResponseEntityDto;
import com.example.monetization.system.dto.response.VideoTopViewResponseDto;
import com.example.monetization.system.dto.response.VideoTopWatchTimeResponseDto;
import com.example.monetization.system.entity.Member;
import com.example.monetization.system.entity.MemberRoleEnum;
import com.example.monetization.system.entity.Video;
import com.example.monetization.system.entity.VideoAd;
import com.example.monetization.system.repository.read.Read_MemberRepository;
import com.example.monetization.system.repository.read.Read_VideoAdRepository;
import com.example.monetization.system.repository.read.Read_VideoRepository;
import com.example.monetization.system.repository.read.calculate.Read_AdCalculateRepository;
import com.example.monetization.system.repository.read.calculate.Read_VideoCalculateRepository;
import com.example.monetization.system.repository.read.statisitcs.Read_VideoStatisticsRepository;
import com.example.monetization.system.repository.write.Write_MemberRepository;
import com.example.monetization.system.security.MemberDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final PasswordEncoder passwordEncoder;

    private final Write_MemberRepository write_memberRepository;

    private final Read_MemberRepository read_memberRepository;
    private final Read_VideoRepository read_videoRepository;
    private final Read_VideoStatisticsRepository read_videoStatisticsRepository;
    private final Read_VideoCalculateRepository read_videoCalculateRepository;
    private final Read_VideoAdRepository read_videoAdRepository;
    private final Read_AdCalculateRepository read_adCalculateRepository;

    public ResponseEntity<ResponseEntityDto<Void>> signup(SignupRequestDto signupRequestDto) {
        String password = passwordEncoder.encode(signupRequestDto.getPassword());

        // 회원 중복 확인
        Optional<Member> checkMember = read_memberRepository.findByMemberEmail(signupRequestDto.getMemberEmail());
        if (checkMember.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseEntityDto<>("중복된 사용자가 존재합니다."));
        }

        MemberRoleEnum authority = MemberRoleEnum.BUYER;
        if (signupRequestDto.getAuthority()) authority = MemberRoleEnum.SELLER;

        // 회원 가입 확인
        Member member = new Member(signupRequestDto.getMemberEmail(),
                password,
                authority,
                signupRequestDto.getSocial());
        write_memberRepository.save(member);

        return ResponseEntity.ok(new ResponseEntityDto<>("회원가입 성공"));
    }

    public List<VideoTopViewResponseDto> topView(String period, MemberDetailsImpl memberDetails) {
        Member member = memberDetails.getMember();
        LocalDate[] dateRanges = dateRange(period);

        Pageable top5 = PageRequest.of(0, 5);
        return read_videoRepository.findTop5ViewVideosByMemberAndDateRange(member, dateRanges[0], dateRanges[1], top5);
    }

    public List<VideoTopWatchTimeResponseDto> topWatchTime(String period, MemberDetailsImpl memberDetails) {
        Member member = memberDetails.getMember();

        LocalDate[] dateRanges = dateRange(period);
        Pageable top5 = PageRequest.of(0, 5);
        return read_videoStatisticsRepository.findTop5WatchTimeVideosByMemberAndDateRange(member, dateRanges[0], dateRanges[1], top5);
    }

    public List<CalculateResponseDto> calculate(String period, MemberDetailsImpl memberDetails) {
        Member member = memberDetails.getMember();

        // 조회 기간
        LocalDate[] dateRanges = dateRange(period);

        // 반환할 List 객체
        List<CalculateResponseDto> calculateResponseDtoList = new ArrayList<>();

        // 해당 member의 등록 video 조회
        List<Video> videoList = read_videoRepository.findAllByMember(member);

        // 비디오와 정산 금액을 조회
        List<VideoAmountDto> videoCalculateList = read_videoCalculateRepository.findAllByVideoAndDateRange(videoList, dateRanges[0], dateRanges[1]);

        for(VideoAmountDto videoAmountDto : videoCalculateList) {
            Video video = videoAmountDto.getVideo();

            List<VideoAd> videoAdList = read_videoAdRepository.findAllByVideo(video);
            Long adAmount  = read_adCalculateRepository.findAmountByVideoAdListAndDateRange(videoAdList, dateRanges[0], dateRanges[1]);

            CalculateResponseDto calculateResponseDto = new CalculateResponseDto();
            calculateResponseDto.setVideoId(video.getVideoId());
            calculateResponseDto.setVideoName(video.getVideoName());
            calculateResponseDto.setVideoAmount(videoAmountDto.getVideoAmount());
            calculateResponseDto.setAdAmount(adAmount);
            calculateResponseDto.setTotalAmount(videoAmountDto.getVideoAmount()+adAmount);

            calculateResponseDtoList.add(calculateResponseDto);
        }

        return calculateResponseDtoList;
    }


    private LocalDate[] dateRange(String period) {
        LocalDate[] dateRange = new LocalDate[2];

        LocalDate today = LocalDate.now();

        switch (period) {
            case "day":
                dateRange[0] = today;
                dateRange[1] = today;
                return dateRange;
            case "week":
                // 이번 주 월요일
                dateRange[0] = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
                // 이번 주 일요일
                dateRange[1] = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
                return dateRange;
            case "month":
                // 이번 달의 첫 날
                dateRange[0] = today.with(TemporalAdjusters.firstDayOfMonth());
                // 이번 달의 마지막 날
                dateRange[1] = today.with(TemporalAdjusters.lastDayOfMonth());
                return dateRange;
            default:
                throw new IllegalArgumentException("Invalid period. Must be 'day', 'week', or 'month'.");
        }
    }
}
