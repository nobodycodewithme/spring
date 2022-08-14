package com.nobodycodewithme.springautoconfig;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.metrics.mongo.MongoMetricsAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {MongoMetricsAutoConfiguration.class})
public class SpringautoconfigApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringautoconfigApplication.class, args);
    }

}
