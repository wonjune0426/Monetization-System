package com.example.monetization.system.dto.response;

import lombok.Data;

import java.util.UUID;

@Data
public class CalculateResponseDto {
    private UUID videoId;
    private String videoName;
    private Long videoAmount;
    private Long adAmount;
    private Long totalAmount;
}
