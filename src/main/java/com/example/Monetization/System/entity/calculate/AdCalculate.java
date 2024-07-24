package com.example.monetization.system.entity.calculate;


import com.example.monetization.system.entity.VideoAd;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AdCalculate{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long adCalculateId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_ad_id")
    private VideoAd videoAd;

    @Column(nullable = false)
    private Long adAmount;

    @Column(nullable = false)
    private LocalDate createdAt = LocalDate.now().minusDays(1);

}
