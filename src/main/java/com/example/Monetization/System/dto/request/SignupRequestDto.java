package com.example.Monetization.System.dto.request;

import lombok.Data;

@Data
public class SignupRequestDto {
    private String member_id;
    private String password;
    private boolean authority;
    private String social;
}
