package com.example.Monetization.System.service;

import com.example.Monetization.System.dto.request.member.SignupRequestDto;
import com.example.Monetization.System.entity.Member;
import com.example.Monetization.System.entity.MemberRoleEnum;
import com.example.Monetization.System.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
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

}
