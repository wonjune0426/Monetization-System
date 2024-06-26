package com.example.Monetization.System.repository;

import com.example.Monetization.System.entity.VideoView_history;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VideoView_historyRepository extends JpaRepository<VideoView_history, UUID> {
}
