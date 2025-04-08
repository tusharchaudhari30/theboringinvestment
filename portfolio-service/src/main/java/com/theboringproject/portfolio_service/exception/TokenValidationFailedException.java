package com.theboringproject.portfolio_service.exception;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@NoArgsConstructor
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class TokenValidationFailedException extends RuntimeException {

    public TokenValidationFailedException(String message) {
        super(message);
    }

}