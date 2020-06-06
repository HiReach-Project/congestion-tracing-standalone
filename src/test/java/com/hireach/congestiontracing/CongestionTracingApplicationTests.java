package com.hireach.congestiontracing;

import com.hireach.congestiontracing.service.DeviceLocationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.time.Instant;

@SpringBootTest
class CongestionTracingApplicationTests {

    @Autowired
    DeviceLocationService deviceLocationService;

    @Test
    @Rollback(value = false)
    void contextLoads() {
        Instant instant = Instant.now();
        System.out.println(instant);
        deviceLocationService.saveTrace(84.3333D, 172.2222D, "sx", "zz");
    }

}
