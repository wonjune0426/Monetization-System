package com.example.monetization.system.repository.write;

import com.example.monetization.system.entity.VideoAd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Write_VideoAdRepository extends JpaRepository<VideoAd, Long> {

}
