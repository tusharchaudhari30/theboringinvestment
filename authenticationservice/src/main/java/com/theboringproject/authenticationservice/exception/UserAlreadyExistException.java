package com.theboringproject.authenticationservice.exception;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@NoArgsConstructor
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserAlreadyExistException extends UserCustomException {
    public UserAlreadyExistException(String message) {
        super(message);
    }
}