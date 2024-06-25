package com.example.Monetization.System.repository;

import com.example.Monetization.System.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VideoRepository extends JpaRepository<Video, UUID> {
}
