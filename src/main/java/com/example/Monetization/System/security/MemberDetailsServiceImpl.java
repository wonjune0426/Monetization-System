package com.example.Monetization.System.security;

import com.example.Monetization.System.entity.Member;
import com.example.Monetization.System.repository.MemberRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MemberDetailsServiceImpl implements UserDetailsService {

    private final MemberRepository memberRepository;

    public MemberDetailsServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String memberId) throws UsernameNotFoundException {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new UsernameNotFoundException("Not Found" + memberId)
        );
        return new MemberDetailsImpl(member);
    }
}
