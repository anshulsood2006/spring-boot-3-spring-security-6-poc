package com.arsoft.projects.thevibgyor.backend.exception.handler;

import com.arsoft.projects.thevibgyor.backend.exception.Error;
import com.arsoft.projects.thevibgyor.backend.exception.TheVibgyorException;
import com.arsoft.projects.thevibgyor.backend.model.Header;
import com.arsoft.projects.thevibgyor.common.util.HttpRequestUtil;
import com.arsoft.projects.thevibgyor.common.util.TimeUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.ZonedDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public TheVibgyorException handleMethodNotSupportedException(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        ZonedDateTime startZonedDateTime = ZonedDateTime.now();
        String url = HttpRequestUtil.getFullURL(request);
        int statusCode = HttpStatus.FORBIDDEN.value();
        response.setStatus(statusCode);
        ZonedDateTime endZonedDateTime = ZonedDateTime.now();
        long elapsedTime = TimeUtil.getElapsedTimeInMillis(startZonedDateTime, endZonedDateTime);
        Header header = new Header(url, startZonedDateTime, endZonedDateTime, elapsedTime, statusCode);
        Error error = new Error(header, "You are using an unsupported HTTP method to call the endpoint.");
        log.info("Handling 405 method not supported exception");
        return new TheVibgyorException(error);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public TheVibgyorException handleNoHandlerFound(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        ZonedDateTime startZonedDateTime = ZonedDateTime.now();
        String url = HttpRequestUtil.getFullURL(request);
        int statusCode = HttpStatus.FORBIDDEN.value();
        response.setStatus(statusCode);
        ZonedDateTime endZonedDateTime = ZonedDateTime.now();
        long elapsedTime = TimeUtil.getElapsedTimeInMillis(startZonedDateTime, endZonedDateTime);
        Header header = new Header(url, startZonedDateTime, endZonedDateTime, elapsedTime, statusCode);
        Error error = new Error(header, "You are trying to call a non existing endpoint.");
        log.info("Handling 404 page not found exception");
        return new TheVibgyorException(error);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public TheVibgyorException handleHttpMessageNotReadableException(HttpServletRequest request, HttpServletResponse response, HttpMessageNotReadableException ex) {
        ZonedDateTime startZonedDateTime = ZonedDateTime.now();
        String url = HttpRequestUtil.getFullURL(request);
        int statusCode = HttpStatus.FORBIDDEN.value();
        response.setStatus(statusCode);
        ZonedDateTime endZonedDateTime = ZonedDateTime.now();
        long elapsedTime = TimeUtil.getElapsedTimeInMillis(startZonedDateTime, endZonedDateTime);
        Header header = new Header(url, startZonedDateTime, endZonedDateTime, elapsedTime, statusCode);
        Error error = new Error(header, "Your request is not valid. Cause: ." + ex.getMessage());
        log.info("Handling 404 page not found exception");
        return new TheVibgyorException(error);
    }
}
