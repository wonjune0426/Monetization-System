package com.example.Monetization.System.repository;

import com.example.Monetization.System.entity.Member;
import com.example.Monetization.System.entity.Video;
import com.example.Monetization.System.entity.VideoView_History;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface VideoView_HistoryRepository extends JpaRepository<VideoView_History, UUID> {
    Optional<VideoView_History> findByMemberAndVideo(Member member, Video video);
}
