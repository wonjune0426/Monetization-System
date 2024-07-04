package com.example.Monetization.System.configuration;

import com.example.Monetization.System.entity.Calculate;
import com.example.Monetization.System.repository.VideoRepository;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.*;

@Configuration
@RequiredArgsConstructor
public class JobConfig {

    private final VideoRepository videoRepository;
    private final EntityManagerFactory serviceEntityManagerFactory;

    @Bean
    public Job sampleJob(JobRepository jobRepository, Step todayVideoViewCountStep) {
        return new JobBuilder("Calculate", jobRepository)
                .start(todayVideoViewCountStep)
                .incrementer(new RunIdIncrementer())
                .build();
    }


    @Bean
    public Step todayVideoViewCountStep(JobRepository jobRepository, @Qualifier("SERVICE_TRANSACTION_MANAGER") PlatformTransactionManager serviceTransactionManager,
                                   ItemReader<HashMap<String, Object>> itemReader) {
        return new StepBuilder("videoViewCount", jobRepository)
                .<HashMap<String, Object>, Calculate>chunk(10, serviceTransactionManager)
                .reader(itemReader)
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean
    public JpaPagingItemReader<HashMap<String, Object>> reader() {
        return new JpaPagingItemReaderBuilder<HashMap<String, Object>>()
                .name("videoViewReader")
                .entityManagerFactory(serviceEntityManagerFactory)
                .queryString("SELECT new map(v.video.video_id as video_id, SUM(v.view_count) as view_count) " +
                        "FROM VideoView_History v WHERE DATE(v.updated_at) = DATE('2024-03-14') GROUP BY v.video.video_id")
                .pageSize(10)
                .build();
    }

    @Bean
    public ItemProcessor<HashMap<String, Object>, Calculate> processor() {
        return items -> new Calculate(videoRepository.findById(UUID.fromString(items.get("video_id").toString())).orElseThrow(),
                (Long) items.get("view_count"));
    }

    @Bean
    public JpaItemWriter<Calculate> writer() {
        JpaItemWriter<Calculate> jpaItemWriter = new JpaItemWriter<>();
        jpaItemWriter.setEntityManagerFactory(serviceEntityManagerFactory);
        return jpaItemWriter;
    }


}