package com.example.Monetization.System.configuration;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class SpringBatchConfig {


    @Bean
    public Job trMigrationJob(JobRepository jobRepository, Step sampleStep) {
        return new JobBuilder("sampleJob", jobRepository)
                .start(sampleStep)
                .build();
    }

    @Bean
    public Step trMigrationStep(JobRepository jobRepository, Tasklet sampleTasklet,PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("sample step", jobRepository)
                .tasklet(sampleTasklet, platformTransactionManager)
                .build();
    }

    @Bean
    public Tasklet sampleTasklet() {
        return new Tasklet(){

            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                System.out.println("sampleTasklet");
                return RepeatStatus.FINISHED;
            }
        };
    }

}
