package com.portfolio.management.service;

import com.portfolio.management.model.Portfolio;
import com.portfolio.management.model.Stock;
import com.portfolio.management.model.User;
import com.portfolio.management.repository.PortfolioRepository;
import com.portfolio.management.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final StockRepository stockRepository;
    private final UserService userService;
    private PortfolioAnalysisStrategy analysisStrategy;

    @Autowired
    public PortfolioService(PortfolioRepository portfolioRepository, StockRepository stockRepository, UserService userService) {
        this.portfolioRepository = portfolioRepository;
        this.stockRepository = stockRepository;
        this.userService = userService;
        // Default strategy
        this.analysisStrategy = new PerformanceAnalysisStrategy();
    }

    // Strategy pattern: set the analysis strategy
    public void setAnalysisStrategy(PortfolioAnalysisStrategy strategy) {
        this.analysisStrategy = strategy;
    }

    // Use the current strategy to analyze the portfolio
    public double analyzePortfolio(Portfolio portfolio) {
        return analysisStrategy.analyze(portfolio);
    }

    public Portfolio getCurrentUserPortfolio() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return portfolioRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Portfolio not found"));
    }

    @Transactional
    public void addStock(Stock stock) {
        Portfolio portfolio = getCurrentUserPortfolio();

        // Check if user already has this stock
        Optional<Stock> existingStock = stockRepository.findByPortfolioAndSymbol(portfolio, stock.getSymbol());

        if (existingStock.isPresent()) {
            // Update existing stock
            Stock currentStock = existingStock.get();
            double totalQuantity = currentStock.getQuantity() + stock.getQuantity();
            double totalCost = (currentStock.getQuantity() * currentStock.getPurchasePrice()) + (stock.getQuantity() * stock.getPurchasePrice());
            double averagePrice = totalCost / totalQuantity;

            currentStock.setQuantity(totalQuantity);
            currentStock.setPurchasePrice(averagePrice);
            currentStock.setCurrentPrice(stock.getCurrentPrice());
            stockRepository.save(currentStock);
        } else {
            // Add new stock
            stock.setPortfolio(portfolio);
            stockRepository.save(stock);
        }

        // Update portfolio total value
        double totalAmount = stock.getQuantity() * stock.getPurchasePrice();
        portfolio.setAvailableCash(portfolio.getAvailableCash() - totalAmount);
        portfolio.calculateTotalValue();
        portfolioRepository.save(portfolio);
    }

    @Transactional
    public void removeStock(Long stockId) {
        Stock stock = stockRepository.findById(stockId)
                .orElseThrow(() -> new RuntimeException("Stock not found"));

        Portfolio portfolio = stock.getPortfolio();

        // Add the value back to available cash
        double stockValue = stock.getQuantity() * stock.getCurrentPrice();
        portfolio.setAvailableCash(portfolio.getAvailableCash() + stockValue);

        // IMPORTANT: Remove stock from portfolio's collection first
        portfolio.getStocks().remove(stock);
        
        // Remove the stock
        stockRepository.delete(stock);

        // Update portfolio total value
        portfolio.calculateTotalValue();
        portfolioRepository.save(portfolio);
    }

    @Transactional
    public void updateStock(Stock updatedStock) {
        Stock stock = stockRepository.findById(updatedStock.getId())
                .orElseThrow(() -> new RuntimeException("Stock not found"));

        stock.setName(updatedStock.getName());
        stock.setQuantity(updatedStock.getQuantity());
        stock.setCurrentPrice(updatedStock.getCurrentPrice());

        stockRepository.save(stock);

        // Update portfolio total value
        Portfolio portfolio = stock.getPortfolio();
        portfolio.calculateTotalValue();
        portfolioRepository.save(portfolio);
    }

    public Stock getStockById(Long id) {
        return stockRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Stock not found"));
    }

    public List<Stock> getAllStocksForCurrentUser() {
        Portfolio portfolio = getCurrentUserPortfolio();
        return stockRepository.findByPortfolio(portfolio);
    }
}
