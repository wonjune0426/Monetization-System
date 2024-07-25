package com.example.monetization.system.entity;


import com.example.monetization.system.entity.timestapm.CreateTimestamped;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "adview_history")
public class AdViewHistory extends CreateTimestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long adviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_ad_id")
    private VideoAd videoAd;

    public AdViewHistory(VideoAd videoAd) {
        this.videoAd = videoAd;
    }
}
