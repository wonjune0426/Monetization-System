package com.example.Monetization.System.service;

import com.example.Monetization.System.dto.request.SignupRequestDto;
import com.example.Monetization.System.entity.Member;
import com.example.Monetization.System.jwt.JwtUtil;
import com.example.Monetization.System.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RedisTemplate<String,String> redisTemplate;

    public void signup(SignupRequestDto signupRequestDto) {
        String password = passwordEncoder.encode(signupRequestDto.getPassword());

        // 회원 중복 확인
        Optional<Member> checkMember = memberRepository.findById(signupRequestDto.getMember_id());
        if(checkMember.isPresent()) {
            throw new IllegalStateException("중복된 사용자가 존재합니다.");
        };

        // 회원 가입 확인
        Member member = new Member(signupRequestDto.getMember_id(),password,signupRequestDto.isAuthority(),false,signupRequestDto.getSocial());
        memberRepository.save(member);
    }

}
