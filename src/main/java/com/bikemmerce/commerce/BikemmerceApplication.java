package com.bikemmerce.commerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class BikemmerceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BikemmerceApplication.class, args);
    }

}
