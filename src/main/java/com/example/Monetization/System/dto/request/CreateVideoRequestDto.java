package com.example.Monetization.System.dto.request;

import lombok.Data;

@Data
public class CreateVideoRequestDto {
    private String video_name;
    private long video_length;
    private String video_description;
}
