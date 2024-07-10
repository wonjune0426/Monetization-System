package com.example.Monetization.System.repository.statisitcs;

import com.example.Monetization.System.entity.statisitcs.VideoStatistics;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface VideoStatisticsRepository extends JpaRepository<VideoStatistics, Long> {
}
