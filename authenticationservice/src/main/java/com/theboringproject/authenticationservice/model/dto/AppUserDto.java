package com.theboringproject.authenticationservice.model.dto;

import lombok.Data;

@Data
public class AppUserDto {
    private String email;
    private String password;
}
