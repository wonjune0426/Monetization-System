package com.example.monetization.system.repository.read.statisitcs;

import com.example.monetization.system.entity.statisitcs.AdStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Read_AdStatisticsRepository extends JpaRepository<AdStatistics, Long> {

}
