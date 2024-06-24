package com.example.Monetization.System.service;

import com.example.Monetization.System.dto.request.LoginRequestDto;
import com.example.Monetization.System.dto.request.SignupRequestDto;
import com.example.Monetization.System.entity.Member;
import com.example.Monetization.System.jwt.JwtUtil;
import com.example.Monetization.System.repository.MemberRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public void signup(SignupRequestDto signupRequestDto) {
        String password = passwordEncoder.encode(signupRequestDto.getPassword());

        // 회원 중복 확인
        Optional<Member> checkMember = memberRepository.findById(signupRequestDto.getMember_id());
        if(checkMember.isPresent()) {
            throw new IllegalStateException("중복된 사용자가 존재합니다.");
        }

        // 회원 가입 확인
        Member member = new Member(signupRequestDto.getMember_id(),password,signupRequestDto.isAuthority(),signupRequestDto.getProfile(),false,signupRequestDto.getSocial());
        memberRepository.save(member);
    }

    public void login(LoginRequestDto loginRequestDto, HttpServletResponse res) {

        // 사용자
        Member member = memberRepository.findById(loginRequestDto.getMember_id()).orElseThrow(
                ()-> new IllegalArgumentException("등록된 사용자가 없습니다.")
        );

        // 비밀번호 확인
        if(!passwordEncoder.matches(loginRequestDto.getPassword(), member.getPassword())){
            throw new IllegalStateException("비밀번호가 일치하지 않음");
        }

        // JWT 생성 및 Cookie에 저장 후 Response 객체에 추가
        String token = jwtUtil.createToken(member.getMember_id(), member.isAuthority());
    }
}
