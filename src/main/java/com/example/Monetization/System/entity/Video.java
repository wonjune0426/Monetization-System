package com.example.Monetization.System.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "video")
public class Video extends Timestamped {

    @Id
    private UUID video_id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private String video_name;

    private String video_length;

    private String video_description;

    private Long likes;

    private boolean delete_check;


}
