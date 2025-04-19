package com.theboringproject.portfolio_service.service;

import com.theboringproject.portfolio_service.model.dao.Transaction;
import com.theboringproject.portfolio_service.model.dto.Portfolio;
import com.theboringproject.portfolio_service.model.dto.Stock;
import com.theboringproject.portfolio_service.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class PortfolioService {

    private final TransactionRepository transactionRepository;
    private final AuthenticationService authenticationService;

    public PortfolioService(AuthenticationService authenticationService, TransactionRepository transactionRepository) {
        this.authenticationService = authenticationService;
        this.transactionRepository = transactionRepository;
    }

    public Portfolio getPortfolio(String token) {
        Long userid = authenticationService.validate(token).getId();
        log.info("Retrieving portfolio for user {}", userid);

        List<Transaction> transactionList = transactionRepository.findAllByUserId(userid);
        Portfolio portfolio = new Portfolio();
        Map<String, Stock> quantityMap = new HashMap<>();

        if (!transactionList.isEmpty()) {
            transactionList.forEach(transaction -> {
                quantityMap.compute(transaction.getTicker(), (ticker, stock) -> {
                    if (stock == null) {
                        return new Stock(
                                transaction.getAssetName(),
                                transaction.getTicker(),
                                transaction.getAverage(),
                                500.0, // dummy current price
                                transaction.getQuantity());
                    } else {
                        int totalQuantity = stock.quantity + transaction.getQuantity();
                        double newAverage = ((stock.average * stock.quantity)
                                + (transaction.getAverage() * transaction.getQuantity())) / totalQuantity;
                        stock.quantity = totalQuantity;
                        stock.average = newAverage;
                        return stock;
                    }
                });
            });

            portfolio.transactionList = transactionList;
            quantityMap.values().forEach(portfolio::addStock);

            portfolio.profit = portfolio.holding - portfolio.invested;
            portfolio.percentageReturn = (portfolio.invested != 0) ? (portfolio.profit / portfolio.invested) * 100
                    : 0.0;
        } else {
            portfolio.profit = 0.0;
            portfolio.percentageReturn = 0.0;
        }

        log.info("Retrieved portfolio for user {} with profit {} and percentage return {}", userid, portfolio.profit,
                portfolio.percentageReturn);
        return portfolio;
    }
}
