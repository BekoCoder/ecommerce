package com.example.ecommerce.exception;

public class ProductNotEnoughException extends RuntimeException{
    public ProductNotEnoughException(String message) {
        super(message);
    }
}
