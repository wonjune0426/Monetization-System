package com.example.monetization.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
//@SpringBootApplication(exclude = SecurityAutoConfiguration.class)  // Spring Security 인증 기능 제외
public class MonetizationSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(MonetizationSystemApplication.class, args);
	}

}
