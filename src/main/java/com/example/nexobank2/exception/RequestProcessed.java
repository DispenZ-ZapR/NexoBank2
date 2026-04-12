package com.example.nexobank2.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RequestProcessed extends BaseException{
    public RequestProcessed(String message) {
        super(message);
    }
}
