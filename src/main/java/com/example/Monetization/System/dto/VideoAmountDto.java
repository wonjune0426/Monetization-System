package com.example.monetization.system.dto;


import com.example.monetization.system.entity.Video;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VideoAmountDto {
    private Video video;
    private Long videoAmount;
}
