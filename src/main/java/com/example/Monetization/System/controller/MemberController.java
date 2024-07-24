package com.example.monetization.system.controller;


import com.example.monetization.system.dto.request.member.SignupRequestDto;
import com.example.monetization.system.dto.response.CalculateResponseDto;
import com.example.monetization.system.dto.response.ResponseEntityDto;
import com.example.monetization.system.dto.response.VideoTopViewResponseDto;
import com.example.monetization.system.dto.response.VideoTopWatchTimeResponseDto;
import com.example.monetization.system.entity.MemberRoleEnum;
import com.example.monetization.system.security.MemberDetailsImpl;
import com.example.monetization.system.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;
    private static final List<String> VALID_PERIODS = Arrays.asList("day", "week", "month");

    @PostMapping("/sign-up")
    public ResponseEntity<ResponseEntityDto<Void>> signUp(@RequestBody @Valid SignupRequestDto signupRequestDto) {
        return memberService.signup(signupRequestDto);
    }

    @GetMapping("/videos/top-view/{period}")
    @Secured(MemberRoleEnum.Authority.SELLER)
    public ResponseEntity<ResponseEntityDto<List<VideoTopViewResponseDto>>> topView(@PathVariable(name = "period") String period, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        if (!VALID_PERIODS.contains(period)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseEntityDto<>("day, week, month의 기간만 조회 가능합니다."));
        }
        return ResponseEntity.ok(new ResponseEntityDto<>("누적 조회수 Top5", memberService.topView(period, memberDetails)));
    }
    @GetMapping("/videos/top-watchtime/{period}")
    @Secured(MemberRoleEnum.Authority.SELLER)
    public ResponseEntity<ResponseEntityDto<List<VideoTopWatchTimeResponseDto>>> topWatchTime(@PathVariable(name = "period") String period, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        if (!VALID_PERIODS.contains(period)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseEntityDto<>("day, week, month의 기간만 조회 가능합니다."));
        }
        return ResponseEntity.ok(new ResponseEntityDto<>("누적 재성 시간 Top5", memberService.topWatchTime(period, memberDetails)));
    }

    @GetMapping("/calculate/{period}")
    @Secured(MemberRoleEnum.Authority.SELLER)
    public ResponseEntity<ResponseEntityDto<List<CalculateResponseDto>>> calculate(@PathVariable(name = "period") String period, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        if (!VALID_PERIODS.contains(period)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseEntityDto<>("day, week, month의 기간만 조회 가능합니다."));
        }
        return ResponseEntity.ok(new ResponseEntityDto<>("정산 금액 현황", memberService.calculate(period, memberDetails)));
    }


}
