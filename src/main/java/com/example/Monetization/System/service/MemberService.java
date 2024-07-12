package com.example.Monetization.System.service;

import com.example.Monetization.System.dto.request.member.SignupRequestDto;
import com.example.Monetization.System.dto.response.VideoTopViewResponseDto;
import com.example.Monetization.System.dto.response.VideoTopWatchTimeResponseDto;
import com.example.Monetization.System.entity.Member;
import com.example.Monetization.System.entity.MemberRoleEnum;
import com.example.Monetization.System.repository.MemberRepository;
import com.example.Monetization.System.repository.VideoRepository;
import com.example.Monetization.System.security.MemberDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final VideoRepository videoRepository;
    private final PasswordEncoder passwordEncoder;

    public String signup(SignupRequestDto signupRequestDto) {
        String password = passwordEncoder.encode(signupRequestDto.getPassword());

        // 회원 중복 확인
        Optional<Member> checkMember = memberRepository.findById(signupRequestDto.getMemberId());
        if (checkMember.isPresent()) {
            return "중복된 사용자가 존재합니다.";
        };


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

        LocalDate today = LocalDate.now();
        LocalDate startDate;
        LocalDate endDate;

        switch (period) {
            case "day":
                startDate = today;
                endDate = today;
                break;
            case "week":
                // 이번 주 월요일
                startDate = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
                // 이번 주 일요일
                endDate = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
                break;
            case "month":
                // 이번 달의 첫 날
                startDate = today.with(TemporalAdjusters.firstDayOfMonth());
                // 이번 달의 마지막 날
                endDate = today.with(TemporalAdjusters.lastDayOfMonth());
                break;
            default:
                throw new IllegalArgumentException("Invalid period. Must be 'day', 'week', or 'month'.");
        }

        Pageable top5 = PageRequest.of(0, 5);
        return videoRepository.findTop5ViewVideosByMemberAndDateRange(member,startDate,endDate,top5);
    }

    public List<VideoTopWatchTimeResponseDto> topWatchTime(String period, MemberDetailsImpl memberDetails) {
        Member member = memberDetails.getMember();

        LocalDate today = LocalDate.now();
        LocalDate startDate;
        LocalDate endDate;

        switch (period) {
            case "day":
                startDate = today;
                endDate = today;
                break;
            case "week":
                // 이번 주 월요일
                startDate = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
                // 이번 주 일요일
                endDate = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
                break;
            case "month":
                // 이번 달의 첫 날
                startDate = today.with(TemporalAdjusters.firstDayOfMonth());
                // 이번 달의 마지막 날
                endDate = today.with(TemporalAdjusters.lastDayOfMonth());
                break;
            default:
                throw new IllegalArgumentException("Invalid period. Must be 'day', 'week', or 'month'.");
        }

        Pageable top5 = PageRequest.of(0, 5);
        return videoRepository.findTop5WatchTimeVideosByMemberAndDateRange(member,startDate,endDate,top5);
    }
}
