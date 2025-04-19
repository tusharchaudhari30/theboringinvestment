package com.theboringproject.authenticationservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.theboringproject.authenticationservice.model.dao.User;
import com.theboringproject.authenticationservice.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserDetailService {
    final UserRepository userRepository;

    public UserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<String> getUserDetails() {
        log.info("Getting user list from User Details");
        return userRepository.findAll().stream().map(User::getEmail).collect(Collectors.toList());
    }
}