package com.example.Monetization.System.repository;

import com.example.Monetization.System.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member,String> {
}
