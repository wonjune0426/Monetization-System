package com.example.Monetization.System.repository;

import com.example.Monetization.System.entity.AdView_history;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AdViewRepository extends JpaRepository<AdView_history, UUID> {
}
