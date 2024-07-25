package com.example.monetization.system.repository.read;

import com.example.monetization.system.entity.Ad;
import com.example.monetization.system.entity.Video;
import com.example.monetization.system.entity.VideoAd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface Read_VideoAdRepository extends JpaRepository<VideoAd, Long> {
    Optional<VideoAd> findVideoAdByVideoAndAd(Video video, Ad ad);

    List<VideoAd> findAllByAd(Ad ad);

    List<VideoAd> findAllByVideo(Video video);
}
