package com.example.Monetization.System.dto;

import com.example.Monetization.System.entity.Video;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VideoAmountDto {
    private Video video;
    private Long videoAmount;
}
