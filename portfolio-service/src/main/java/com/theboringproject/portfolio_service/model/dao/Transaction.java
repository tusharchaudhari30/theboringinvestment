package com.theboringproject.portfolio_service.model.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Entity
@Table(name = "transactions")
@Data
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String assetName;

    @Column(nullable = false)
    private String ticker;

    @Column(nullable = false)
    private Double average;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date transactionDate = new Date();

    public Transaction(String assetName, String ticker, Double average, Integer quantity) {
        this.assetName = assetName;
        this.ticker = ticker;
        this.average = average;
        this.quantity = quantity;
    }

    public Transaction() {
    }
}
