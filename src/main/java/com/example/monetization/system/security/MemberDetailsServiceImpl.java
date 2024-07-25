package com.example.monetization.system.security;

import com.example.monetization.system.entity.Member;
import com.example.monetization.system.repository.read.Read_MemberRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MemberDetailsServiceImpl implements UserDetailsService {

    private final Read_MemberRepository read_memberRepository;

    public MemberDetailsServiceImpl(Read_MemberRepository memberRepository) {
        this.read_memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String memberEmail) throws UsernameNotFoundException {
        Member member = read_memberRepository.findByMemberEmail(memberEmail).orElseThrow(
                () -> new UsernameNotFoundException("Not Found" + memberEmail)
        );
        return new MemberDetailsImpl(member);
    }
}
