package com.example.ecommerce.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AlreadyExistException.class)
    public ResponseEntity<String> handleAlreadyExistException(AlreadyExistException ex) {
        return new ResponseEntity<>("Bu foydalanuvchi ro'yhatdan o'tgan", HttpStatus.CONFLICT);
    }

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<String> handleDataNotFoundException(DataNotFoundException ex) {
        return new ResponseEntity<>("Ma'lumot topilmadi", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<String> productNotFound(ProductNotFoundException ex) {
        return new ResponseEntity<>("Mahsulot topilmadi", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> userNotFound(UserNotFoundException ex) {
        return new ResponseEntity<>("User o'chirilgan", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<String> fileNotFound(FileNotFoundException ex) {
        return new ResponseEntity<>("Fayl o'chirilgan yoki mavjud emas", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CategoryException.class)
    public ResponseEntity<String> handleCategoryException(CategoryException ex) {
        return new ResponseEntity<>("Kategoriya topilmadi", HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(ProductNotEnoughException.class)
    public ResponseEntity<String> handleProductNotEnoughException(ProductNotEnoughException ex) {
        return new ResponseEntity<>("Mahsulot yetarli emas", HttpStatus.NOT_FOUND);
    }

}
