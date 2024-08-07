package com.example.monetization.system.entity;

import com.example.monetization.system.entity.timestapm.MainTimestamped;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Member extends MainTimestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(nullable = false)
    private String memberEmail;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private MemberRoleEnum authority;

    @Column(length = 20)
    private String social;

    @Column(nullable = false)
    private Boolean deleteCheck;

    public Member(String memberEmail, String password, MemberRoleEnum authority, String social) {
        this.memberEmail = memberEmail;
        this.password = password;
        this.authority = authority;
        this.social = social;
        this.deleteCheck = false;
    }
}
