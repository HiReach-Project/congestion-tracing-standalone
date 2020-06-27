package com.hireach.congestiontracing.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.Instant;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeviceLocation {

    @Id
    private String deviceId;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    private Instant updatedAt;

    private Point locationPoint;

}
