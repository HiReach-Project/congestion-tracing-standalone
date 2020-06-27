package com.hireach.congestiontracing;

import com.hireach.congestiontracing.service.DeviceLocationService;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.time.Instant;

import static org.apache.commons.codec.digest.MessageDigestAlgorithms.SHA3_256;

//@SpringBootTest
class CongestionTracingApplicationTests {

    @Autowired
    DeviceLocationService deviceLocationService;

    @Test
    @Rollback(value = false)
    void contextLoads() {
        String sha3_256hex = new DigestUtils(SHA3_256).digestAsHex("HM4fQAQmLF36LCWEctCuJrEtqTtgegpG");
        System.out.println(sha3_256hex);
//        Instant instant = Instant.now();
//        System.out.println(instant);
//        // bucharest
//        //deviceLocationService.saveTrace(44.426164962 , 26.102332924, "sx", "zz");
//        // cluj
//        deviceLocationService.saveOrUpdateDeviceLocation(46.7666700, 23.6000000, "sx", "zz", Instant.now());
    }

}
