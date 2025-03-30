package com.theboringproject.portfolio_service.model.dto;

import java.io.Serializable;

public class StockPrice implements Serializable {
    private String stockSymbol;
    private double price;

    public StockPrice() {
    }

    public StockPrice(String stockSymbol, double price) {
        this.stockSymbol = stockSymbol;
        this.price = price;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public void setStockSymbol(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "StockPrice{" +
                "stockSymbol='" + stockSymbol + '\'' +
                ", price=" + price +
                '}';
    }
}
