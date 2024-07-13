package com.example.Monetization.System.repository.calculate;

import com.example.Monetization.System.entity.VideoAd;
import com.example.Monetization.System.entity.calculate.AdCalculate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.time.LocalDate;
import java.util.List;

public interface AdCalculateRepository extends JpaRepository<AdCalculate,Long> {
    @Query(value = "SELECT COALESCE(SUM(ac.adAmount), 0) " +
            "FROM AdCalculate ac " +
            "WHERE ac.videoAd in (:videoList) AND " +
            "ac.createdAt BETWEEN :startDate AND :endDate")
    Long findAmountByVideoAdListAndDateRange(@Param("videoList") List<VideoAd> videoList,
                                             @Param("startDate") LocalDate startDate, @P("endDate") LocalDate endDate);

}
