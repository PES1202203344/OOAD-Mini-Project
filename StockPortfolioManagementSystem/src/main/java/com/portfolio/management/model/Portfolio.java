package com.portfolio.management.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Portfolio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private User user;

    @OneToMany(mappedBy = "portfolio", cascade = CascadeType.ALL)
    private List<Stock> stocks = new ArrayList<>();

    private double totalValue;
    private double availableCash = 10000.0; // Default starting cash

    // Method to calculate total portfolio value
    public void calculateTotalValue() {
        double stocksValue = stocks.stream()
                .mapToDouble(stock -> stock.getCurrentPrice() * stock.getQuantity())
                .sum();
        this.totalValue = stocksValue + availableCash;
    }
}
