package com.theboringproject.authenticationservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.theboringproject.authenticationservice.model.dao.User;
import com.theboringproject.authenticationservice.model.dto.AppUserDto;
import com.theboringproject.authenticationservice.service.AuthenticationService;

@RestController
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    public AuthenticationController(@Autowired AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @GetMapping("/validate")
    public ResponseEntity<User> verify(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(authenticationService.validate(token));
    }

    @GetMapping("/login")
    public ResponseEntity<?> login(@RequestParam String email, @RequestParam String password) {
        return ResponseEntity.ok(authenticationService.login(email, password));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUpUser(@RequestBody AppUserDto appUserDto) {
        return ResponseEntity.ok(authenticationService.saveUser(appUserDto));
    }
}
