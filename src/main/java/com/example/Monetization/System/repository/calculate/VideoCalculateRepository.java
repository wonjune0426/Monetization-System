package com.example.Monetization.System.repository.calculate;

import com.example.Monetization.System.dto.VideoAmountDto;
import com.example.Monetization.System.entity.Video;
import com.example.Monetization.System.entity.calculate.VideoCalculate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface VideoCalculateRepository extends JpaRepository<VideoCalculate, Long> {

    @Query(value = "SELECT new com.example.Monetization.System.dto.VideoAmountDto(vc.video, sum(vc.videoAmount) as videoAmount) " +
            "FROM VideoCalculate vc " +
            "WHERE vc.video in :videoList AND vc.createdAt BETWEEN :startDate AND :endDate " +
            "GROUP BY vc.video")
    List<VideoAmountDto> findAllByVideoAndDateRange(@Param("videoList") List<Video> videoList, @Param("startDate") LocalDate startDate,
                                                    @Param("endDate") LocalDate endDate);
}
