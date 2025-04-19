package com.theboringproject.transaction_service.repository;

import com.theboringproject.transaction_service.model.dao.Transaction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findAllByUserIdOrderByTransactionDateDesc(Long userid, Pageable pageable);

    List<Transaction> findAllByUserId(Long userid);

    void deleteById(Long id);

    Integer countByUserId(Long userid);
}