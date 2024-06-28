package com.example.Monetization.System.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL) // NULL 값은 제외함
public class VideoViewResponseDto {
    private String video_name;
    private String video_length;
    private String video_description;
    private String last_watch_time;

    private String errorMessage;
}
