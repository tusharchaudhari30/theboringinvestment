package com.theboringproject.authenticationservice.model.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AuthenticationResponse {
    private String username;
    private boolean success;
    private String message;
    private String token;
}
