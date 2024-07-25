package com.example.monetization.system.dto.request.member;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDto {
   @NotBlank(message = "ID를 입력해주세요")
   private String memberEmail;
   @NotBlank(message = "password를 입력해주세요")
   private String password;
}
