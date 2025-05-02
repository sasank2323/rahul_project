package com.app.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class ExceptionHandling {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> getException(
            Exception exp, WebRequest web
    ){
        ExceptionDto expDto = new ExceptionDto();
        expDto.setDate(new Date());
        expDto.setMessage(exp.getMessage());
        expDto.setRequest(web.getDescription(false));
        return new ResponseEntity<>(expDto, HttpStatus.NOT_FOUND);
    }
}
