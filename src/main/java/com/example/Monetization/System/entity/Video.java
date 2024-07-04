package com.example.Monetization.System.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "video")
public class Video extends Timestamped {

    @Id
    private UUID video_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String video_name;

    private String video_description;

    private Long video_length;

    private Long total_playtime;

    private boolean delete_check;


}
