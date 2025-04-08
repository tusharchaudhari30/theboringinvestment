package com.theboringproject.authenticationservice.service;

import com.theboringproject.authenticationservice.exception.TokenValidationFailedException;
import com.theboringproject.authenticationservice.exception.UserAlreadyExistException;
import com.theboringproject.authenticationservice.exception.UserCustomException;
import com.theboringproject.authenticationservice.exception.UserNotFoundException;
import com.theboringproject.authenticationservice.model.dao.User;
import com.theboringproject.authenticationservice.model.dto.AppUserDto;
import com.theboringproject.authenticationservice.model.dto.ToastMessage;
import com.theboringproject.authenticationservice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class AuthenticationService {

    private final UserRepository userRepository;
    private final JwtUtilService jwtUtilService;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationService(UserRepository userRepository, JwtUtilService jwtUtilService,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtUtilService = jwtUtilService;
        this.passwordEncoder = passwordEncoder;
    }

    public User validate(String token) {
        token = token.substring(7); // remove "Bearer "
        try {
            String email = jwtUtilService.getSubjectFromToken(token);
            User user = userRepository.findUserByEmail(email);
            if (user == null)
                throw new UserNotFoundException("User not found.");
            log.info("Successfully validated token for user {}", email);
            return user;
        } catch (Exception e) {
            log.error("Error validating token [{}]: {}", token, e.getMessage(), e);
            throw new TokenValidationFailedException("Invalid token");
        }
    }

    public Map<String, String> login(String email, String password) {
        User user = userRepository.findUserByEmail(email);
        if (user == null)
            throw new UserNotFoundException("User not found.");
        if (!passwordEncoder.matches(password, user.getPassword())) {
            log.error("Invalid password for user [{}]", email);
            throw new UserCustomException("Invalid password");
        }

        String token = jwtUtilService.generateToken(email);
        log.info("Successfully logged in user [{}]", email);
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        return response;
    }

    public ToastMessage saveUser(AppUserDto appUserDto) {
        if (userRepository.findUserByEmail(appUserDto.getEmail()) != null) {
            log.error("User already exists [{}]", appUserDto.getEmail());
            throw new UserAlreadyExistException("User already exists.");
        }

        User user = new User(appUserDto.getEmail(), passwordEncoder.encode(appUserDto.getPassword()));
        userRepository.save(user);
        log.info("Successfully registered user [{}]", user.getEmail());
        return new ToastMessage("done");
    }
}
