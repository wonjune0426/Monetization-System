package com.example.Monetization.System.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "calculate")
public class Calculate extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long calculate_id;

    @ManyToOne
    @JoinColumn(name = "video_id")
    private Video video;

    private Long total_videoview;

    private Long total_vdeoamount;

    private Long total_adview;

    private Long total_adamount;

    private Long calculate_sum;

}
