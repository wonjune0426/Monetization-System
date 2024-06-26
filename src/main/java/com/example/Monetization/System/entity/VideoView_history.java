package com.example.Monetization.System.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "videoview_history")
public class VideoView_history {
    @Id
    private UUID videoview_id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "video_id")
    private Video video;

    @CreatedDate
    private LocalDateTime create_at;

    public VideoView_history(UUID uuid, Member member, Video video) {
        this.videoview_id = uuid;
        this.member = member;
        this.video = video;
    }
}
