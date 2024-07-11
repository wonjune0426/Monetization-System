package com.example.Monetization.System.repository;

import com.example.Monetization.System.entity.Ad;
import com.example.Monetization.System.entity.Video;
import com.example.Monetization.System.entity.VideoAd;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VideoAdRepository extends JpaRepository<VideoAd, Long> {
    Optional<VideoAd> findVideoAdByVideoAndAd(Video video, Ad ad);

    List<VideoAd> findAllByAd(Ad ad);

    List<VideoAd> findAllByVideo(Video video);
}
