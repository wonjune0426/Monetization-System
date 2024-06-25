package com.example.Monetization.System.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignupRequestDto {

    @NotBlank (message = "Id는 공란일 수 없습니다.")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식에 맞지 않습니다.")
    private String member_id;

    @Size(min = 8, max = 15,message = "비밀번호는 최소 8글자부터 최대 15글자입니다.")
    private String password;

    private boolean authority;
    private String social;
}
