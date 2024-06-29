package com.example.Monetization.System.dto.request;

import lombok.Data;

@Data
public class CreateAdRequestDto {
    private String ad_name;
    private String ad_description;
    private Long ad_price;
}
