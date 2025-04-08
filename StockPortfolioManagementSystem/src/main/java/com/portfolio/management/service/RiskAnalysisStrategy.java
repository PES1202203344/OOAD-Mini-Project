package com.portfolio.management.service;

import com.portfolio.management.model.Portfolio;

// Concrete Strategy: Risk Analysis
public class RiskAnalysisStrategy implements PortfolioAnalysisStrategy {
    @Override
    public double analyze(Portfolio portfolio) {
        // Simple risk score based on portfolio diversification
        int numberOfStocks = portfolio.getStocks().size();
        double totalValue = portfolio.getTotalValue();
        
        if (numberOfStocks == 0) return 0;
        
        // Calculate concentration of each stock
        double maxConcentration = portfolio.getStocks().stream()
                .mapToDouble(stock -> (stock.getCurrentPrice() * stock.getQuantity()) / totalValue)
                .max()
                .orElse(0);
        
        // Risk score from 0-100 (higher is riskier)
        return (maxConcentration * 100) - (Math.min(numberOfStocks, 20) * 2);
    }
}