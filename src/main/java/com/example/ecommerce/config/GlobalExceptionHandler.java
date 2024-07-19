package com.example.ecommerce.config;

import com.example.ecommerce.exception.AlreadyExistException;
import com.example.ecommerce.exception.DataNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AlreadyExistException.class)
    public ResponseEntity<String> handleAlreadyExistException(AlreadyExistException ex) {
        return new ResponseEntity<>("Bu foydalanuvchi Ro'yhatdan o'tgan", HttpStatus.CONFLICT);
    }

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<String> handleDataNotFoundException(DataNotFoundException ex) {
        return new ResponseEntity<>("Ma'lumot topilmadi", HttpStatus.NOT_FOUND);
    }


}
