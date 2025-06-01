package com.theboringproject.portfolio_service.service;

import com.theboringproject.portfolio_service.model.dao.Transaction;
import com.theboringproject.portfolio_service.model.dao.User;
import com.theboringproject.portfolio_service.model.dto.ChartModel;
import com.theboringproject.portfolio_service.model.dto.Portfolio;
import com.theboringproject.portfolio_service.model.dto.Stock;
import com.theboringproject.portfolio_service.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class PortfolioService {

    private final TransactionRepository transactionRepository;
    private final AuthenticationService authenticationService;
    private final RedisTemplate<String, Portfolio> portfolioRedisTemplate;

    public PortfolioService(AuthenticationService authenticationService, TransactionRepository transactionRepository,
            RedisTemplate<String, Portfolio> portfolioRedisTemplate) {
        this.authenticationService = authenticationService;
        this.transactionRepository = transactionRepository;
        this.portfolioRedisTemplate = portfolioRedisTemplate;
    }

    public Portfolio getPortfolio(String token) {
        User user = authenticationService.validate(token);
        Long userId = user.getId();
        log.info("Retrieving portfolio for user {}", userId);
        List<Transaction> transactionList = transactionRepository.findAllByUserId(userId);
        Portfolio portfolio = new Portfolio();
        Map<String, Stock> quantityMap = new HashMap<>();
        Map<String, Double> stockPriceMap = new HashMap<>();
        if (!transactionList.isEmpty()) {
            transactionList.forEach(transaction -> {
                String ticker = transaction.getTicker();
                // Dummy current price – replace with real fetch later
                stockPriceMap.putIfAbsent(ticker, 500.0);
                Double currentPrice = stockPriceMap.get(ticker);
                quantityMap.compute(ticker, (key, stock) -> {
                    if (stock == null) {
                        double changePercent = (currentPrice - transaction.getAverage()) / transaction.getAverage()
                                * 100;
                        return new Stock(
                                transaction.getAssetName(),
                                ticker,
                                transaction.getAverage(),
                                currentPrice,
                                transaction.getQuantity(),
                                changePercent);
                    } else {
                        int totalQuantity = stock.getQuantity() + transaction.getQuantity();
                        double newAverage = ((stock.getAverage() * stock.getQuantity())
                                + (transaction.getAverage() * transaction.getQuantity())) / totalQuantity;
                        stock.setQuantity(totalQuantity);
                        stock.setAverage(newAverage);
                        stock.setPrice(currentPrice);
                        double changePercent = (currentPrice - newAverage) / newAverage * 100;
                        stock.setChangePercent(changePercent);
                        return stock;
                    }
                });
            });
            portfolio.setTransactionList(transactionList);
            quantityMap.values().forEach(portfolio::addStock);
            portfolio.setProfit(portfolio.getHolding() - portfolio.getInvested());
            portfolio.setPercentageReturn(portfolio.getInvested() != 0
                    ? (portfolio.getProfit() / portfolio.getInvested()) * 100
                    : 0.0);
        } else {
            portfolio.setProfit(0.0);
            portfolio.setPercentageReturn(0.0);
        }
        portfolioRedisTemplate.opsForValue().set("Portfolio " + user.getEmail(), portfolio, 30, TimeUnit.MINUTES);
        log.info("Retrieved portfolio for user {} with profit {} and percentage return {}",
                userId, portfolio.getProfit(), portfolio.getPercentageReturn());
        return portfolio;
    }

    public Portfolio updateStockPrice(String email, Portfolio portfolio, String ticker, Double newPrice) {
        log.info("Updating stock price for ticker: {}", ticker);
        // Find the stock in the portfolio
        Stock stockToUpdate = portfolio.getStockList().stream()
                .filter(stock -> stock.getTicker().equals(ticker))
                .findFirst()
                .orElse(null);
        if (stockToUpdate != null) {
            // Update the stock's price
            stockToUpdate.setPrice(newPrice);
            // Recalculate the change percentage
            double changePercent = (newPrice - stockToUpdate.getAverage()) / stockToUpdate.getAverage() * 100;
            stockToUpdate.setChangePercent(changePercent);

            // Update the stock in the portfolio
            double newHolding = portfolio.getStockList().stream()
                    .mapToDouble(stock -> stock.getPrice() * stock.getQuantity())
                    .sum();
            portfolio.setHolding(newHolding);

            // Recalculate profit and percentage return if needed
            portfolio.setProfit(portfolio.getHolding() - portfolio.getInvested());
            portfolio.setPercentageReturn(portfolio.getInvested() != 0
                    ? (portfolio.getProfit() / portfolio.getInvested()) * 100
                    : 0.0);
            List<ChartModel> chartData = new ArrayList<>();
            for (Stock stock : portfolio.getStockList()) {
                chartData.add(new ChartModel(stock.getAssetName(), stock.getPrice() * stock.getQuantity()));
            }
            portfolio.setChartModels(chartData);
            log.info("Updated stock price for ticker {}: New price = {}, Change percent = {}",
                    ticker, newPrice, changePercent);
            portfolioRedisTemplate.opsForValue().set(email, portfolio, 30, TimeUnit.MINUTES);
        } else {
            log.warn("Stock with ticker {} not found in the portfolio.", ticker);
        }
        return portfolio;
    }

}
