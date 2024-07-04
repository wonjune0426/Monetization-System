package com.example.Monetization.System.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "calculate")
public class Calculate extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long calculate_id;

    @ManyToOne
    @JoinColumn(name = "video_id")
    private Video video;

    private Long total_videoview;

    private Long total_videoamount;

    private Long total_adview;

    private Long total_adamount;

    private Long calculate_sum;

    public Calculate(Video video,Long total_videoview){
        this.video=video;
        this.total_videoview=total_videoview;
    }

}
