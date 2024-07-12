package com.example.Monetization.System.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class VideoTopViewResponseDto {
    private UUID videoId;
    private String videoName;
    private String videoDescription;
    private Long videoLength;
    private Long totalView;
}


