package com.example.monetization.system.dto.request.ad;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class AdAddRequestDto {
    private List<UUID> videoIds;
}
