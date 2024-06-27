package com.example.Monetization.System.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "adview_history")
@EntityListeners(AuditingEntityListener.class)
public class AdView_history {
    @Id
    private UUID adview_id;

    @ManyToOne
    @JoinColumn(name="video_ad_info_id")
    private Video_Ad_Info video_ad_info;

    @CreatedDate
    private String create_at;

    public AdView_history(UUID adview_id, Video_Ad_Info video_ad_info) {
        this.adview_id = adview_id;
        this.video_ad_info = video_ad_info;
    }

    @PrePersist  // 저장하기 전에 실행
    public void onPrePersist() {
        this.create_at = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
    }
}
