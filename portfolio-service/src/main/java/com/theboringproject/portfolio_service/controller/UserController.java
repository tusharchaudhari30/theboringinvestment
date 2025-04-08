package com.theboringproject.portfolio_service.controller;

import com.theboringproject.portfolio_service.model.dao.Assets;
import com.theboringproject.portfolio_service.model.dto.AppUserDto;
import com.theboringproject.portfolio_service.model.dto.Portfolio;
import com.theboringproject.portfolio_service.repository.AssetRepository;
import com.theboringproject.portfolio_service.service.AuthenticationService;
import com.theboringproject.portfolio_service.service.PortfolioService;
import com.theboringproject.portfolio_service.model.dao.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

    private final AuthenticationService authenticationService;
    private final AssetRepository assetRepository;
    private final PortfolioService portfolioService;

    public UserController(AuthenticationService authenticationService, AssetRepository assetRepository,
            PortfolioService portfolioService) {
        this.authenticationService = authenticationService;
        this.assetRepository = assetRepository;
        this.portfolioService = portfolioService;
    }

    @GetMapping
    public ResponseEntity<User> hello(@RequestHeader("Authorization") String token) {
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

    @GetMapping("/portfolio")
    public Portfolio getPortfolio(@RequestHeader("Authorization") String token) {
        return portfolioService.getPortfolio(token);
    }

    @GetMapping("/asset/search")
    public List<Assets> searchAssetByName(@RequestParam String keyword) {
        return assetRepository.findByAssetNameContainingIgnoreCase(keyword);
    }
}
