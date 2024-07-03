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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class JobConfig {


    @Bean
    public Job sampleJob(JobRepository jobRepository, Step sampleStep) {
        return new JobBuilder("sampleJob",jobRepository)
                .start(sampleStep).build();
    }

    @Bean
    public Step sampleStep(JobRepository jobRepository, Tasklet sampleTasklet, @Qualifier("SERVICE_TRANSACTION_MANAGER") PlatformTransactionManager serviceTransactionManager) {
        return new StepBuilder("sampleStep",jobRepository)
                .tasklet(sampleTasklet,serviceTransactionManager)
                .build();
    }

    @Bean
    public Tasklet sampleTasklet() {
        return new Tasklet() {

            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                System.out.println("========================sample Tasklet============================");
                return RepeatStatus.FINISHED;
            }
        };
    }
}
