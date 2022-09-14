package com.techelevator.tenmo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class NotAccountOwnerException extends RuntimeException{

    public NotAccountOwnerException(String message){
        super(message);
    }

}
