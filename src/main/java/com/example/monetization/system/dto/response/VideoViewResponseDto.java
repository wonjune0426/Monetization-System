package com.example.monetization.system.dto.response;

import lombok.Data;

@Data
public class VideoViewResponseDto {
    private String videoName;
    private Long videoLength;
    private String videoDescription;
    private Long lastWatchTime;
}
