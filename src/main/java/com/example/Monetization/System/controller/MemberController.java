package com.example.Monetization.System.controller;

import com.example.Monetization.System.dto.request.LoginRequestDto;
import com.example.Monetization.System.dto.request.SignupRequestDto;
import com.example.Monetization.System.jwt.JwtUtil;
import com.example.Monetization.System.service.MemberService;
import jakarta.servlet.http.HttpServletResponse;
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

    private final JwtUtil jwtUtil;
    private final MemberService memberService;

    @PostMapping("/sign-up")
    public String signUp(@RequestBody SignupRequestDto signupRequestDto) {
        memberService.signup(signupRequestDto);
        return "회원가입 성공";
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequestDto loginRequestDto,HttpServletResponse res){
        try {
            memberService.login(loginRequestDto,res);
        } catch (Exception e) {
            log.error(e.getMessage());
            return "fail";
        }
        return "로그인 성공";
    }

}
