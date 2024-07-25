package com.example.monetization.system.repository.write;

import com.example.monetization.system.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Write_MemberRepository extends JpaRepository<Member,Long> {
}
