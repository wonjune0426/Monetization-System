package com.example.Monetization.System.controller;

import com.example.Monetization.System.dto.request.member.SignupRequestDto;
import com.example.Monetization.System.entity.MemberRoleEnum;
import com.example.Monetization.System.security.MemberDetailsImpl;
import com.example.Monetization.System.service.MemberService;
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
    public String signUp(@RequestBody @Valid SignupRequestDto signupRequestDto) {
        return memberService.signup(signupRequestDto);
    }

    @GetMapping("/videos/top-view/{period}")
    @Secured(MemberRoleEnum.Authority.SELLER)
    public ResponseEntity<?> topView(@PathVariable(name = "period") String period,@AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        if (!VALID_PERIODS.contains(period)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid period. Must be 'day', 'week', or 'month'.");
        }
        return ResponseEntity.ok( memberService.topView(period, memberDetails));

    }

    @GetMapping("/videos/top-watchtime/{period}")
    @Secured(MemberRoleEnum.Authority.SELLER)
    public ResponseEntity<?> topWatchTime(@PathVariable(name = "period")String period, @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        if (!VALID_PERIODS.contains(period)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid period. Must be 'day', 'week', or 'month'.");
        }
        return ResponseEntity.ok( memberService.topWatchTime(period, memberDetails));
    }

    @GetMapping("/calculate/{period}")
    @Secured(MemberRoleEnum.Authority.SELLER)
    public ResponseEntity<?> calculate(@PathVariable(name = "period")String period, @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        if (!VALID_PERIODS.contains(period)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid period. Must be 'day', 'week', or 'month'.");
        }
        return ResponseEntity.ok( memberService.calculate(period, memberDetails));
    }





}
