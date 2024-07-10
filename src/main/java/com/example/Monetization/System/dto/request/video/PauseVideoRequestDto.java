package com.example.Monetization.System.dto.request.video;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PauseVideoRequestDto {
    @NotNull(message = "마지막 시청길이가 전달되지 않았습니다.")
    private Long pauseTime;
}
