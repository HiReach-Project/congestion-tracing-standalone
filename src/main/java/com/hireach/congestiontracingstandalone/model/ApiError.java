package com.hireach.congestiontracingstandalone.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiError {

    private HttpStatus status;
    private int code;
    private String message;

}
