package com.theboringproject.transaction_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import com.theboringproject.transaction_service.model.dao.User;

import org.springframework.http.ResponseEntity;

@FeignClient(name = "authentication-service", url = "${services.authentication.url}")
public interface AuthServiceClient {

    @GetMapping(value = "/validate", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<User> validateToken(@RequestHeader("Authorization") String token);
}