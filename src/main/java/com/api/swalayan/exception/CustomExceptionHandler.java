package com.api.swalayan.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
@RequiredArgsConstructor
public class CustomExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, Object> handleInvalidrequest(MethodArgumentNotValidException ex){
        Map<String,Object> errormap=new HashMap<>();
        Map<String,Object> validation=new HashMap<>();
        errormap.put("time", String.valueOf(new Date()));
        errormap.put("success",false);
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        ex.getBindingResult().getFieldErrors().forEach(fieldError -> validation.put(fieldError.getField(), fieldError.getDefaultMessage()));
        errormap.put("message",validation);
        return errormap;
    }

    private String getRequestUrl() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            return request.getRequestURL().toString();
        } catch (IllegalStateException e) {
            return "No Request Available";
        }
    }
    private String getControllerMethodName(Exception ex) {
        StackTraceElement[] stackTrace = ex.getStackTrace();
        for (StackTraceElement element : stackTrace) {
            if (element.getClassName().startsWith("com.api.kkn.app.controllers")) {
                return String.format("%s.%s (line %d)",
                        element.getClassName(),
                        element.getMethodName(),
                        element.getLineNumber());
            }
        }
        return "Unknown source";
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageConversionException.class)
    public Map<String, Object> handleInvalidMessageConversion(HttpMessageConversionException ex){
        Map<String,Object> errormap=new HashMap<>();
        errormap.put("time", String.valueOf(new Date()));
        errormap.put("success",false);
        errormap.put("message",ex.getMessage());
        return errormap;
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Map<String, Object> handleInvalidRequestMethod(HttpRequestMethodNotSupportedException ex){
        Map<String,Object> errormap=new HashMap<>();
        errormap.put("time", String.valueOf(new Date()));
        errormap.put("success",false);
        errormap.put("message",ex.getMessage());
        return errormap;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public Map<String, Object> handleValueNull(SQLIntegrityConstraintViolationException ex){
        Map<String,Object> errormap=new HashMap<>();
        errormap.put("time", String.valueOf(new Date()));
        errormap.put("success",false);
        String errorMessage = ex.getMessage();
        if(errorMessage.contains("Duplicate entry")) {
            errormap.put("message", "The data you entered already exists.");
        } else if(errorMessage.contains("cannot be null")) {
            errormap.put("message", "Some data fields cannot be empty.");
        } else {
            errormap.put("message", "A data validation error occurred. Please check again.");
        }
        return errormap;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Map<String,Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        Map<String,Object> errormap=new HashMap<>();
        errormap.put("time", String.valueOf(new Date()));
        errormap.put("status",HttpStatus.BAD_REQUEST);
        errormap.put("error","Request Body Is Missing");
        return errormap;
    }
}
