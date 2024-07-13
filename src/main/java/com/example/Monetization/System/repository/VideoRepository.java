package com.example.Monetization.System.repository;

import com.example.Monetization.System.dto.response.VideoTopViewResponseDto;
import com.example.Monetization.System.entity.Member;
import com.example.Monetization.System.entity.Video;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface VideoRepository extends JpaRepository<Video, UUID> {


    @Query("SELECT new com.example.Monetization.System.dto.response.VideoTopViewResponseDto(v.videoId, v.videoName, v.videoDescription, v.videoLength,v.totalView) " +
            "FROM Video v " +
            "WHERE v.member = :member " +
            "AND v.createdAt BETWEEN :startDate AND :endDate " +
            "ORDER BY v.totalView DESC")
    List<VideoTopViewResponseDto> findTop5ViewVideosByMemberAndDateRange(
            @Param("member") Member member,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable
    );

    List<Video> findAllByMember(Member member);
}
