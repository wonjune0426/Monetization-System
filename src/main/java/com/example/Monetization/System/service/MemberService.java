package com.example.Monetization.System.service;

import com.example.Monetization.System.dto.VideoAmountDto;
import com.example.Monetization.System.dto.request.member.SignupRequestDto;
import com.example.Monetization.System.dto.response.CalculateResponseDto;
import com.example.Monetization.System.dto.response.VideoTopViewResponseDto;
import com.example.Monetization.System.dto.response.VideoTopWatchTimeResponseDto;
import com.example.Monetization.System.entity.Member;
import com.example.Monetization.System.entity.MemberRoleEnum;
import com.example.Monetization.System.entity.Video;
import com.example.Monetization.System.entity.VideoAd;
import com.example.Monetization.System.repository.MemberRepository;
import com.example.Monetization.System.repository.VideoAdRepository;
import com.example.Monetization.System.repository.VideoRepository;
import com.example.Monetization.System.repository.calculate.AdCalculateRepository;
import com.example.Monetization.System.repository.calculate.VideoCalculateRepository;
import com.example.Monetization.System.repository.statisitcs.VideoStatisticsRepository;
import com.example.Monetization.System.security.MemberDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final VideoRepository videoRepository;
    private final VideoStatisticsRepository videoStatisticsRepository;
    private final VideoCalculateRepository videoCalculateRepository;
    private final VideoAdRepository videoAdRepository;
    private final AdCalculateRepository adCalculateRepository;
    private final PasswordEncoder passwordEncoder;

    public String signup(SignupRequestDto signupRequestDto) {
        String password = passwordEncoder.encode(signupRequestDto.getPassword());

        // 회원 중복 확인
        Optional<Member> checkMember = memberRepository.findById(signupRequestDto.getMemberId());
        if (checkMember.isPresent()) {
            return "중복된 사용자가 존재합니다.";
        }
        ;


        MemberRoleEnum authority = MemberRoleEnum.BUYER;
        if (signupRequestDto.getAuthority()) authority = MemberRoleEnum.SELLER;

        // 회원 가입 확인
        Member member = new Member(signupRequestDto.getMemberId(),
                password,
                authority,
                signupRequestDto.getSocial(), false);
        memberRepository.save(member);

        return "회원 가입 성공";
    }

    public List<VideoTopViewResponseDto> topView(String period, MemberDetailsImpl memberDetails) {
        Member member = memberDetails.getMember();
        LocalDate[] dateRanges = dateRange(period);

        Pageable top5 = PageRequest.of(0, 5);
        return videoRepository.findTop5ViewVideosByMemberAndDateRange(member, dateRanges[0], dateRanges[1], top5);
    }

    public List<VideoTopWatchTimeResponseDto> topWatchTime(String period, MemberDetailsImpl memberDetails) {
        Member member = memberDetails.getMember();

        LocalDate[] dateRanges = dateRange(period);
        Pageable top5 = PageRequest.of(0, 5);
        return videoStatisticsRepository.findTop5WatchTimeVideosByMemberAndDateRange(member, dateRanges[0], dateRanges[1], top5);
    }

    public List<CalculateResponseDto> calculate(String period, MemberDetailsImpl memberDetails) {
        Member member = memberDetails.getMember();

        // 조회 기간
        LocalDate[] dateRanges = dateRange(period);

        // 반환할 List 객체
        List<CalculateResponseDto> calculateResponseDtoList = new ArrayList<>();

        // 해당 member의 등록 video 조회
        List<Video> videoList = videoRepository.findAllByMember(member);

        // 비디오와 정산 금액을 조회
        List<VideoAmountDto> videoCalculateList = videoCalculateRepository.findAllByVideoAndDateRange(videoList, dateRanges[0], dateRanges[1]);

        for(VideoAmountDto videoAmountDto : videoCalculateList) {
            Video video = videoAmountDto.getVideo();

            List<VideoAd> videoAdList = videoAdRepository.findAllByVideo(video);
            Long adAmount  = adCalculateRepository.findAmountByVideoAdListAndDateRange(videoAdList, dateRanges[0], dateRanges[1]);

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
