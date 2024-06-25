package com.example.Monetization.System.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignupRequestDto {
    @Email (message = "E-mail 형식이 아닙니다.")
    @NotBlank
    private String member_id;

    @NotBlank
    @Size(min = 8, max = 15,message = "비밀번호는 최소 8글자부터 최대 15글자입니다.")
    private String password;

    private boolean authority;
    private String social;
}
