package com.theboringproject.portfolio_service.controller;

import com.theboringproject.portfolio_service.model.dao.Assets;
import com.theboringproject.portfolio_service.model.dto.Portfolio;
import com.theboringproject.portfolio_service.repository.AssetRepository;
import com.theboringproject.portfolio_service.service.PortfolioService;

import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final AssetRepository assetRepository;
    private final PortfolioService portfolioService;

    @GetMapping("/portfolio")
    public Portfolio getPortfolio(@RequestHeader("Authorization") String token) {
        return portfolioService.getPortfolio(token);
    }

    @GetMapping("/asset/search")
    public List<Assets> searchAssetByName(@RequestParam String keyword) {
        return assetRepository.findByAssetNameContainingIgnoreCase(keyword);
    }
}
