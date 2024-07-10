package com.example.Monetization.System.dto.request.video;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateVideoRequestDto {
    @NotBlank(message = "제목은 공란일 수 없습니다.")
    @Size(max = 33, message = "제목은 33자 이하까지 입력 가능합니다.")
    private String videoName;

    @NotBlank(message = "설명은 공란일 수 없습니다.")
    @Size(max=85,message = "설명은 85자 이하까지 입력 가능합니다.")
    private String videoDescription;
}
