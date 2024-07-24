package com.example.monetization.system.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class VideoTopWatchTimeResponseDto {
    private UUID videoId;
    private String videoName;
    private String videoDescription;
    private Long videoLength;
    private Long totalWatchTime;
}
