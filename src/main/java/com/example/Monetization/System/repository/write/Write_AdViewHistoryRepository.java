package com.example.monetization.system.repository.write;

import com.example.monetization.system.entity.AdViewHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Write_AdViewHistoryRepository extends JpaRepository<AdViewHistory, Long> {
}
