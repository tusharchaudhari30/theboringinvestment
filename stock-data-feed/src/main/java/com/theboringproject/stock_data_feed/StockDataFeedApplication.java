package com.theboringproject.stock_data_feed;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class StockDataFeedApplication {

	public static void main(String[] args) {
		SpringApplication.run(StockDataFeedApplication.class, args);
	}

}
