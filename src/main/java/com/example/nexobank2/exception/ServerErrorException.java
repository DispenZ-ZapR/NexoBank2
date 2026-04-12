package com.example.nexobank2.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ServerErrorException extends BaseException{
    public ServerErrorException(String message) {
        super(message);
    }
}
