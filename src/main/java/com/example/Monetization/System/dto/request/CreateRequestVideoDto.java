package com.example.Monetization.System.dto.request;

import lombok.Data;

import java.sql.Time;

@Data
public class CreateRequestVideoDto {
    private String video_name;
    private Time video_length;
    private String video_description;
}
