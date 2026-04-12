package com.example.nexobank2.exception;

public class BaseException extends RuntimeException{
    public BaseException(String message) {
        super(message);
    }
    
    public BaseException() {
        super();
    }
}
