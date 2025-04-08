package com.theboringproject.authenticationservice.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserCustomException extends RuntimeException {
    public UserCustomException(String message) {
        super(message);
    }
}