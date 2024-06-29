package com.example.Monetization.System.repository;

import com.example.Monetization.System.entity.Ad;
import com.example.Monetization.System.entity.Video;
import com.example.Monetization.System.entity.Video_Ad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface Video_AdRepository extends JpaRepository<Video_Ad, Integer> {
    Optional<Video_Ad> findByVideoAndAd(Video video, Ad ad);
}
