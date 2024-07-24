package com.example.monetization.system.dto.request.ad;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateAdRequestDto {
    @NotBlank(message = "광고 제목은 공란일 수 없습니다.")
    @Size(max = 33,message = "제목은 33이하로 작성가능 합니다.")
    private String adName;
    @NotBlank(message = "광고 설명은 공란일 수 없습니다.")
    private String adDescription;
}
