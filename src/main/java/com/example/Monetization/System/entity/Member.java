package com.example.Monetization.System.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "member")
public class Member extends Timestamped {
    @Id
    private String member_id;

    private String password;

    private boolean authority;

    private boolean delete_check;

    @Column(nullable = true)
    private String social;

}
