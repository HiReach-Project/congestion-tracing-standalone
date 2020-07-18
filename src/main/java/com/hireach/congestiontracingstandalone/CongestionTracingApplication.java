package com.hireach.congestiontracingstandalone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableCaching
@EnableWebMvc
public class CongestionTracingApplication {

    public static void main(String[] args) {
        SpringApplication.run(CongestionTracingApplication.class, args);
    }

}
