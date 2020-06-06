package com.hireach.congestiontracing;

import com.hireach.congestiontracing.service.LocationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.time.Instant;

@SpringBootTest
class CongestionTracingApplicationTests {

    @Autowired
    LocationService locationService;

    @Test
    @Rollback(value = false)
    void contextLoads() {
        Instant instant = Instant.now();
        System.out.println(instant);
//        traceService.saveTrace();
    }

}
