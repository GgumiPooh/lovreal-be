package com.lovreal_be;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(
        scanBasePackages = {
                "com.lovreal_be" // 루트 한 줄이면 충분 (service, config, repository 등 모두 포함)
        }
)
@EnableJpaRepositories(basePackages = "com.lovreal_be.repository")
@EntityScan(basePackages = "com.lovreal_be.domain")
public class LovrealBeApplication {
	public static void main(String[] args) {
		SpringApplication.run(LovrealBeApplication.class, args);
	}
}