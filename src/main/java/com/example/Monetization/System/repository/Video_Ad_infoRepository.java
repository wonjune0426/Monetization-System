package com.example.Monetization.System.repository;

import com.example.Monetization.System.entity.Video;
import com.example.Monetization.System.entity.Video_Ad_Info;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface Video_Ad_infoRepository extends JpaRepository<Video_Ad_Info, UUID> {
    Video_Ad_Info findByVideo(Video video);
}
