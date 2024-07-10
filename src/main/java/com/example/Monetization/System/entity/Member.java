package com.example.Monetization.System.entity;

import com.example.Monetization.System.entity.timestapm.MainTimestamped;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Member extends MainTimestamped {

    @Id
    private String memberId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Boolean authority;

    @Column(length = 20)
    private String social;

    @Column(nullable = false)
    private Boolean deleteCheck;
}
