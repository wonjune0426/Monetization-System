package com.example.Monetization.System.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "member")
public class Member extends Timestamped {
    @Id
    @Column(name = "member_id")
    private String member_id;

    @Column(name = "password")
    private String password;

    @Column(name = "authority")
    private boolean authority;

    @Column(name = "delete_check")
    private boolean delete_check;

    @Column(name="social",nullable = true)
    private String social;

}
