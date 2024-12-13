package com.example.demo;

import com.example.demo.configuration.FtpConfig;
import com.example.demo.security.JwtProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
public class DemoApplication
{

	private static final Logger log = LoggerFactory.getLogger(DemoApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}


	@Bean
	public CommandLineRunner demo() {
		return (args) -> {
			for (int i = 0; i < 5; i++) {
				String t = JwtProvider.generateToken("test");
				log.info("TEST TOKEN: {}", t);
			}

		};
	}
//	@Scheduled(fixedDelay = 5000)
//	public void scheduleFixedDelayTask() {
//		System.out.println("FTP Connection test  result = " + FtpConfig.checkConnection());
//
//	}


}
