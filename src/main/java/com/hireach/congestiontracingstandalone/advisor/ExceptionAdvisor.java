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
        errorInfo.put("timestamp", Instant.now().toEpochMilli());
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
        errorInfo.put("timestamp", Instant.now().toEpochMilli());
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
        errorInfo.put("timestamp", Instant.now().toEpochMilli());
        errorInfo.put("status", HttpStatus.BAD_REQUEST.value());
        errorInfo.put("error", HttpStatus.BAD_REQUEST.getReasonPhrase());
        errorInfo.put("message", ex.getName() + " (" + ex.getValue() + ") should be of type " + ex.getRequiredType());
        errorInfo.put("path", request.getRequestURI());
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
        Map<String, Object> errorInfo = new LinkedHashMap<>();
        errorInfo.put("timestamp", Instant.now().toEpochMilli());
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
        errorInfo.put("timestamp", Instant.now().toEpochMilli());
        errorInfo.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorInfo.put("error", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        errorInfo.put("message", "Oops! Something went wrong on our side.");
        errorInfo.put("path", ((ServletWebRequest) request).getRequest().getRequestURI());
        return new ResponseEntity<>(errorInfo, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
