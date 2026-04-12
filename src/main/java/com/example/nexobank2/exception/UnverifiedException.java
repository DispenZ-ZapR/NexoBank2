package com.example.nexobank2.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnverifiedException extends BaseException{
    public UnverifiedException(String message) {
        super(message);
    }
}
