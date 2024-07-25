package com.example.monetization.system.repository.read.calculate;


import com.example.monetization.system.entity.VideoAd;
import com.example.monetization.system.entity.calculate.AdCalculate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface Read_AdCalculateRepository extends JpaRepository<AdCalculate,Long> {
    @Query(value = "SELECT COALESCE(SUM(ac.adAmount), 0) " +
            "FROM AdCalculate ac " +
            "WHERE ac.videoAd in (:videoList) AND " +
            "ac.createdAt BETWEEN :startDate AND :endDate")
    Long findAmountByVideoAdListAndDateRange(@Param("videoList") List<VideoAd> videoList,
                                             @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

}
