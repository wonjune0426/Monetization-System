package com.example.monetization.system.repository.read;

import com.example.monetization.system.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface Read_MemberRepository extends JpaRepository<Member,Long> {
    Optional<Member> findByMemberEmail(String email);
}
