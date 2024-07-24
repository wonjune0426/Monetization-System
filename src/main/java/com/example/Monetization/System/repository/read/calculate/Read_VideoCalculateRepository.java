package com.example.monetization.system.repository.read.calculate;

import com.example.monetization.system.dto.VideoAmountDto;
import com.example.monetization.system.entity.Video;
import com.example.monetization.system.entity.calculate.VideoCalculate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface Read_VideoCalculateRepository extends JpaRepository<VideoCalculate, Long> {

    @Query(value = "SELECT new com.example.monetization.system.dto.VideoAmountDto(vc.video, sum(vc.videoAmount) as videoAmount) " +
            "FROM VideoCalculate vc " +
            "WHERE vc.video in :videoList AND vc.createdAt BETWEEN :startDate AND :endDate " +
            "GROUP BY vc.video")
    List<VideoAmountDto> findAllByVideoAndDateRange(@Param("videoList") List<Video> videoList, @Param("startDate") LocalDate startDate,
                                                    @Param("endDate") LocalDate endDate);
}
