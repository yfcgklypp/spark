package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;


@EnableScheduling //支持@Scheduled定时任务
@ServletComponentScan
@SpringBootApplication
public class SparkApplication {

	public static void main(String[] args) {
		SpringApplication.run(SparkApplication.class, args);
	}

	//配置一个线程池
	@Bean
	public Executor getExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(5);
		executor.setMaxPoolSize(10);
		executor.setQueueCapacity(25);
		executor.initialize();
		return executor;
	}

}
