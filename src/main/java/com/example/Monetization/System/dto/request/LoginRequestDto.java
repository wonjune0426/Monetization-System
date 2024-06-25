package com.example.Monetization.System.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDto {
   private String member_id;
   private String password;
}
