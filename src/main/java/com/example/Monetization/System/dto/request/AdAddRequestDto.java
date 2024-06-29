package com.example.Monetization.System.dto.request;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class AdAddRequestDto {
    private List<UUID> videoIds;
}
