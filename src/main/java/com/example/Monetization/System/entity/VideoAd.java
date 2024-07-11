package com.example.Monetization.System.entity;

import com.example.Monetization.System.entity.timestapm.CreateTimestamped;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class VideoAd extends CreateTimestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long videoAdId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id")
    private Video video;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="ad_id")
    private Ad ad;

    @Column(nullable = false)
    private Long totalView;

    @Column(nullable = false)
    private Boolean deleteCheck;

    public VideoAd(Video video, Ad ad) {
        this.video = video;
        this.ad = ad;
        this.totalView = 0L;
        this.deleteCheck = false;
    }

    public void viewUpdate(){
        this.totalView++;
    }

    public void delete(){
        this.deleteCheck = true;
    }
}
