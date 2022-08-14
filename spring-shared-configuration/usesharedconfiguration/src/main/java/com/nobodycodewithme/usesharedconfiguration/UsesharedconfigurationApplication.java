package com.nobodycodewithme.usesharedconfiguration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.metrics.mongo.MongoMetricsAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {MongoMetricsAutoConfiguration.class})
public class UsesharedconfigurationApplication {

	public static void main(String[] args) {
		SpringApplication.run(UsesharedconfigurationApplication.class, args);
	}

}
