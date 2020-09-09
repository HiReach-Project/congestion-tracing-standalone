//    Congestion API - a REST API built to track congestion spots and
//    crowded areas using real-time location data from mobile devices.
//
//    Copyright (C) 2020, University Politehnica of Bucharest, member
//    of the HiReach Project consortium <https://hireach-project.eu/>
//    <andrei[dot]gheorghiu[at]upb[dot]ro. This project has received
//    funding from the European Union’s Horizon 2020 research and
//    innovation programme under grant agreement no. 769819.
//
//    This program is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    This program is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with this program.  If not, see <https://www.gnu.org/licenses/>.
package com.hireach.congestiontracingstandalone.advisor;

import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ExceptionAdvisor extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex,
                                                                   HttpHeaders headers,
                                                                   HttpStatus status,
                                                                   WebRequest request) {
        Map<String, Object> errorInfo = new LinkedHashMap<>();
        errorInfo.put("timestamp", Instant.now());
        errorInfo.put("status", HttpStatus.NOT_FOUND.value());
        errorInfo.put("error", HttpStatus.NOT_FOUND.getReasonPhrase());
        errorInfo.put("message", "Path not found");
        errorInfo.put("path", ((ServletWebRequest) request).getRequest().getRequestURI());
        return new ResponseEntity<>(errorInfo, HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
                                                                          HttpHeaders headers,
                                                                          HttpStatus status,
                                                                          WebRequest request) {
        Map<String, Object> errorInfo = new LinkedHashMap<>();
        errorInfo.put("timestamp", Instant.now());
        errorInfo.put("status", HttpStatus.BAD_REQUEST.value());
        errorInfo.put("error", HttpStatus.BAD_REQUEST.getReasonPhrase());
        errorInfo.put("message", "Missing parameter " + ex.getParameterName());
        errorInfo.put("path", ((ServletWebRequest) request).getRequest().getRequestURI());
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
                                                                   HttpServletRequest request) {
        Map<String, Object> errorInfo = new LinkedHashMap<>();
        errorInfo.put("timestamp", Instant.now());
        errorInfo.put("status", HttpStatus.BAD_REQUEST.value());
        errorInfo.put("error", HttpStatus.BAD_REQUEST.getReasonPhrase());
        errorInfo.put("message", ex.getName() + " (" + ex.getValue() + ") should be of type " + ex.getRequiredType());
        errorInfo.put("path", request.getRequestURI());
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
        Map<String, Object> errorInfo = new LinkedHashMap<>();
        errorInfo.put("timestamp", Instant.now());
        errorInfo.put("status", HttpStatus.BAD_REQUEST.value());
        errorInfo.put("error", HttpStatus.BAD_REQUEST.getReasonPhrase());
        errorInfo.put("message", buildConstraintViolationErrorMessage(ex));
        errorInfo.put("path", ((ServletWebRequest) request).getRequest().getRequestURI());
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }

    private String buildConstraintViolationErrorMessage(ConstraintViolationException ex) {
        return ex.getConstraintViolations()
                .stream()
                .map(violation -> {
                    String parameterName = ((PathImpl) violation.getPropertyPath()).getLeafNode().getName();
                    return parameterName + " " + violation.getMessage();
                })
                .collect(Collectors.joining(", "));
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleAll(final Exception ex, final WebRequest request) {
        ex.printStackTrace();
        Map<String, Object> errorInfo = new LinkedHashMap<>();
        errorInfo.put("timestamp", Instant.now());
        errorInfo.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorInfo.put("error", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        errorInfo.put("message", "Oops! Something went wrong on our side.");
        errorInfo.put("path", ((ServletWebRequest) request).getRequest().getRequestURI());
        return new ResponseEntity<>(errorInfo, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
