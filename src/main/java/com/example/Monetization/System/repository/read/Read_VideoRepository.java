package com.example.monetization.system.repository.read;

import com.example.monetization.system.dto.response.VideoTopViewResponseDto;
import com.example.monetization.system.entity.Member;
import com.example.monetization.system.entity.Video;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
@Transactional(readOnly = true)
public interface Read_VideoRepository extends JpaRepository<Video, UUID> {


    @Query("SELECT new com.example.monetization.system.dto.response.VideoTopViewResponseDto(v.videoId, v.videoName, v.videoDescription, v.videoLength,v.totalView) " +
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
