package com.example.Monetization.System.repository;

import com.example.Monetization.System.entity.Ad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AdRepository extends JpaRepository<Ad, UUID> {
}
