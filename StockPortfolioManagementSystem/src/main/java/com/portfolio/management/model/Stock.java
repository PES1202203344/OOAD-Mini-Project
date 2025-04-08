package com.portfolio.management.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String symbol;
    private String name;
    private double quantity;
    private double purchasePrice;
    private double currentPrice;

    @ManyToOne
    private Portfolio portfolio;

    // Calculate current stock value
    public double getCurrentValue() {
        return quantity * currentPrice;
    }

    // Calculate profit/loss
    public double getProfitLoss() {
        return getCurrentValue() - (quantity * purchasePrice);
    }
}
