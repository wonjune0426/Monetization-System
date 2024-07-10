package com.example.Monetization.System.dto.request.member;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDto {
   @NotBlank(message = "ID를 입력해주세요")
   private String memberId;
   @NotBlank(message = "password를 입력해주세요")
   private String password;
}
