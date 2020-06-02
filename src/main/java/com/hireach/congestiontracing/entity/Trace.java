package com.hireach.congestiontracing.entity;

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
public class Trace {
    @Id
    @GeneratedValue(generator = "trace_gen_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "trace_gen_seq", sequenceName = "trace_seq", allocationSize = 1)
    private Long id;

    private String companyAccessKey;

    private String deviceId;

    private Instant updatedAt;

    private Point location;
}
