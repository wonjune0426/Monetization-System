package com.example.Monetization.System.repository.statisitcs;

import com.example.Monetization.System.dto.response.VideoTopWatchTimeResponseDto;
import com.example.Monetization.System.entity.Member;
import com.example.Monetization.System.entity.statisitcs.VideoStatistics;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface VideoStatisticsRepository extends JpaRepository<VideoStatistics, Long> {

    @Query("SELECT new com.example.Monetization.System.dto.response.VideoTopWatchTimeResponseDto(v.videoId, v.videoName, v.videoDescription, v.videoLength, SUM(vs.videoView) as totalWatchTime) " +
            "FROM VideoStatistics vs " +
            "JOIN vs.video v " +
            "WHERE v.member = :member " +
            "AND v.createdAt BETWEEN :startDate AND :endDate " +
            "GROUP BY v.videoId " +
            "ORDER BY SUM(vs.videoView) DESC")
    List<VideoTopWatchTimeResponseDto> findTop5WatchTimeVideosByMemberAndDateRange(@Param("member") Member member, @Param("startDate") LocalDate startDate,
                                                                                   @Param("endDate") LocalDate endDate, Pageable pageable);
}
