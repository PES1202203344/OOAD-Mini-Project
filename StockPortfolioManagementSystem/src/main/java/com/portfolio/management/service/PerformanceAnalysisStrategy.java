package com.portfolio.management.service;

import com.portfolio.management.model.Portfolio;

// Concrete Strategy: Performance Analysis
public class PerformanceAnalysisStrategy implements PortfolioAnalysisStrategy {
    @Override
    public double analyze(Portfolio portfolio) {
        // Calculate overall performance (return percentage)
        double totalInvestment = portfolio.getStocks().stream()
                .mapToDouble(stock -> stock.getPurchasePrice() * stock.getQuantity())
                .sum();
        
        double currentValue = portfolio.getStocks().stream()
                .mapToDouble(stock -> stock.getCurrentPrice() * stock.getQuantity())
                .sum();
        
        if (totalInvestment == 0) return 0;
        
        return ((currentValue - totalInvestment) / totalInvestment) * 100; // Return percentage
    }
}