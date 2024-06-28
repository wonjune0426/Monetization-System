package com.example.Monetization.System.repository;

import com.example.Monetization.System.entity.Member;
import com.example.Monetization.System.entity.Video;
import com.example.Monetization.System.entity.VideoView_history;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface VideoView_historyRepository extends JpaRepository<VideoView_history, UUID> {
    Optional<VideoView_history> findByMemberAndVideo(Member member, Video video);
}
