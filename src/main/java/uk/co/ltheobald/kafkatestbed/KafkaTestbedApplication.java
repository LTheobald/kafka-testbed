package uk.co.ltheobald.kafkatestbed;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class KafkaTestbedApplication {

	public static void main(String[] args) {
		SpringApplication.run(KafkaTestbedApplication.class, args);
	}

}
