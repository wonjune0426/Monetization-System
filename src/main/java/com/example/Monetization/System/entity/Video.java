package com.example.Monetization.System.entity;

import com.example.Monetization.System.entity.timestapm.MainTimestamped;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Video extends MainTimestamped {
    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID videoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false, length = 100)
    private String videoName;

    @Column(nullable = false)
    private String videoDescription;

    @Column(nullable = false)
    private Long videoLength;

    @Column(nullable = false)
    private Long totalView;

    @Column(nullable = false)
    private Boolean deleteCheck;

    public Video(Member member, String videoName, String videoDescription, long videoLength) {
        this.member = member;
        this.videoName = videoName;
        this.videoDescription = videoDescription;
        this.videoLength = videoLength;
        this.totalView = 0L;
        this.deleteCheck = false;
    }

    public void update(String videoName, String videoDescription) {
        this.videoName = videoName;
        this.videoDescription = videoDescription;
    }

    public void delete() {
        this.deleteCheck = true;
    }
}
