package com.example.monetization.system.repository.read.statisitcs;

import com.example.monetization.system.entity.statisitcs.AdStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface Read_AdStatisticsRepository extends JpaRepository<AdStatistics, Long> {

}
