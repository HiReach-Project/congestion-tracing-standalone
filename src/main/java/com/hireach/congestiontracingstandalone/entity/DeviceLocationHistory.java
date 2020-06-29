package com.hireach.congestiontracingstandalone.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeviceLocationHistory {

    @Id
    @GeneratedValue(generator = "device_location_history_gen_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "device_location_history_gen_seq", sequenceName = "device_location_history_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    private String deviceId;

    private Instant updatedAt;

    private Point locationPoint;

}
