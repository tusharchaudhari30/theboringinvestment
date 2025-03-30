package com.theboringproject.portfolio_service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class Stock {
    public String assetName;
    public String ticker;
    public Double average;
    public Double price;
    public Integer quantity;
}