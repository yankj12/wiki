package com.cowork.wiki;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class WikiApplication {

	public static void main(String[] args) {
        SpringApplication.run(WikiApplication.class, args);
    }
}
