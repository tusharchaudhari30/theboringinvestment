package com.theboringproject.transaction_service.service;

import com.theboringproject.transaction_service.model.dao.Transaction;
import com.theboringproject.transaction_service.repository.TransactionRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class TransactionService {
    final TransactionRepository transactionRepository;

    final AuthenticationService authenticationService;

    public TransactionService(AuthenticationService authenticationService,
            TransactionRepository transactionRepository) {
        this.authenticationService = authenticationService;
        this.transactionRepository = transactionRepository;
    }

    public Map<String, Object> getTransactionByToken(String token, Integer page) {

        Map<String, Object> map = new HashMap<>();
        Long userid = authenticationService.validate(token).getId();
        log.info("Retrieving transactions for user {} with page {}", userid, page);
        map.put("transaction",
                transactionRepository.findAllByUserIdOrderByTransactionDateDesc(userid, PageRequest.of(page, 5)));
        Integer transactionCount = transactionRepository.countByUserId(userid);
        long count = transactionCount / 5;
        if (transactionCount % 5 != 0)
            count += 1;
        map.put("pages", Optional.of(count));
        return map;
    }

    // Saves a transaction for a user identified by a token
    public void saveTransaction(String token, Transaction transaction) {
        // validate the token and get the user's email address
        Long userid = authenticationService.validate(token).getId();
        log.info("Saving transaction for user {} with ticker {} and quantity {}", userid, transaction.getTicker(),
                transaction.getQuantity());
        // set the transaction ID to null to ensure a new transaction is created
        transaction.setId(null);
        transaction.setUserId(userid);
        transactionRepository.save(transaction);
        log.info("Saved transaction for user {} with ticker {} and quantity {}", userid, transaction.getTicker(),
                transaction.getQuantity());
    }
}
