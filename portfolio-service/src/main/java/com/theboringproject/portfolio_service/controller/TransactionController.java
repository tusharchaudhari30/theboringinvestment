package com.theboringproject.portfolio_service.controller;

import com.theboringproject.portfolio_service.model.dao.Transaction;
import com.theboringproject.portfolio_service.model.dto.ToastMessage;
import com.theboringproject.portfolio_service.repository.TransactionRepository;
import com.theboringproject.portfolio_service.service.PortfolioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/transaction")
@CrossOrigin
public class TransactionController {

    private final PortfolioService portfolioService;
    private final TransactionRepository transactionRepository;

    public TransactionController(PortfolioService portfolioService, TransactionRepository transactionRepository) {
        this.portfolioService = portfolioService;
        this.transactionRepository = transactionRepository;
    }

    @GetMapping("/{page}")
    public Map<String, Object> getTransactionList(
            @RequestHeader("Authorization") String token,
            @PathVariable("page") int page) {
        return portfolioService.getTransactionByToken(token, page);
    }

    @PostMapping
    public ResponseEntity<ToastMessage> saveTransaction(
            @RequestHeader("Authorization") String token,
            @RequestBody Transaction transaction) {
        if (transaction.getQuantity() == 0 || transaction.getAverage() == 0) {
            return ResponseEntity.status(405).body(new ToastMessage("Failed"));
        }
        portfolioService.saveTransaction(token, transaction);
        return ResponseEntity.ok(new ToastMessage("OK"));
    }

    @DeleteMapping
    public ResponseEntity<String> deleteTransaction(@RequestParam("id") Long id) {
        try {
            transactionRepository.deleteById(id);
            return ResponseEntity.ok("delete");
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.toString());
        }
    }
}
