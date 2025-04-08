// PortfolioObserver.java
package com.portfolio.management.service;

import com.portfolio.management.model.Portfolio;
import com.portfolio.management.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PortfolioObserver implements StockPriceObserver {

    private final StockRepository stockRepository;

    @Autowired
    public PortfolioObserver(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Override
    public void update(String symbol, double price) {
        // Find all stocks with this symbol and update their current price
        stockRepository.findAll().stream()
                .filter(stock -> stock.getSymbol().equalsIgnoreCase(symbol))
                .forEach(stock -> {
                    stock.setCurrentPrice(price);
                    stockRepository.save(stock);
                    // Update the portfolio total value
                    Portfolio portfolio = stock.getPortfolio();
                    portfolio.calculateTotalValue();
                });
    }
}