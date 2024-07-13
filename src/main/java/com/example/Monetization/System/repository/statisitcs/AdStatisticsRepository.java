package com.example.Monetization.System.repository.statisitcs;

import com.example.Monetization.System.dto.response.VideoTopWatchTimeResponseDto;
import com.example.Monetization.System.entity.Member;
import com.example.Monetization.System.entity.statisitcs.AdStatistics;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface AdStatisticsRepository extends JpaRepository<AdStatistics, Long> {

}
