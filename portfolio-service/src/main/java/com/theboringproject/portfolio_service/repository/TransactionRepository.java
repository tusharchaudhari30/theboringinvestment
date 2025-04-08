package com.theboringproject.portfolio_service.repository;

import com.theboringproject.portfolio_service.model.dao.Transaction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findAllByUseridOrderByTransactionDateDesc(String userid, Pageable pageable);

    List<Transaction> findAllByUserid(String userid);

    void deleteById(Long id);

    Integer countByUserid(String userid);
}