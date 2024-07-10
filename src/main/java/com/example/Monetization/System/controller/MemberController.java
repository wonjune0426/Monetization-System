package com.example.Monetization.System.controller;

import com.example.Monetization.System.dto.request.member.SignupRequestDto;
import com.example.Monetization.System.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/sign-up")
    public String signUp(@RequestBody @Valid SignupRequestDto signupRequestDto) {
        return memberService.signup(signupRequestDto);
    }



}
