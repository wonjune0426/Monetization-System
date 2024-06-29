package com.example.Monetization.System.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "video_ad")
public class Video_Ad extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long video_ad_id;

    @ManyToOne
    @JoinColumn(name = "video_id")
    private Video video;

    @ManyToOne
    @JoinColumn(name = "ad_id")
    private Ad ad;

    private Long view_count;


}
