package com.eb.eventsbridge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class EventsBridgeApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventsBridgeApplication.class, args);
	}
	


}
