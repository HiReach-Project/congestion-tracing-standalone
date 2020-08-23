package com.hireach.congestiontracingstandalone.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MLDataModel {

    private OffsetDateTime timestamp;
    private Integer value;

}
