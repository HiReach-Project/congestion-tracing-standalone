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
        // bucharest
        //deviceLocationService.saveTrace(44.426164962 , 26.102332924, "sx", "zz");
        // cluj
        deviceLocationService.saveDeviceLocation(46.7666700, 23.6000000, "sx", "zz");
    }

}
