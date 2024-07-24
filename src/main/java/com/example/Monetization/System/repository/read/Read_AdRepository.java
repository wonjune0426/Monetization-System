package com.example.monetization.system.repository.read;

import com.example.monetization.system.entity.Ad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface Read_AdRepository extends JpaRepository<Ad, UUID> {
}
