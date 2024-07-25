package com.example.monetization.system.repository.write;

import com.example.monetization.system.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface Write_VideoRepository extends JpaRepository<Video, UUID> {

}
