package com.portfolio.management.service;

import com.portfolio.management.model.*;
import com.portfolio.management.repository.PortfolioRepository;
import com.portfolio.management.repository.StockRepository;
import com.portfolio.management.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final PortfolioRepository portfolioRepository;
    private final StockRepository stockRepository;
    private final UserService userService;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, PortfolioRepository portfolioRepository,
                             StockRepository stockRepository, UserService userService) {
        this.transactionRepository = transactionRepository;
        this.portfolioRepository = portfolioRepository;
        this.stockRepository = stockRepository;
        this.userService = userService;
    }

    @Transactional
    public void executeBuyTransaction(String symbol, String name, double quantity, double price) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Portfolio portfolio = portfolioRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Portfolio not found"));

        double totalAmount = quantity * price;

        // Check if user has enough cash
        if (portfolio.getAvailableCash() < totalAmount) {
            throw new RuntimeException("Insufficient funds for this transaction");
        }

        // Create transaction using the factory pattern
        Transaction transaction = TransactionFactory.createBuyTransaction(user, symbol, name, quantity, price);
        transactionRepository.save(transaction);

        // Update or add stock to portfolio
        Optional<Stock> existingStockOpt = stockRepository.findByPortfolioAndSymbol(portfolio, symbol);

        if (existingStockOpt.isPresent()) {
            // Update existing stock
            Stock existingStock = existingStockOpt.get();
            double totalQuantity = existingStock.getQuantity() + quantity;
            double totalCost = (existingStock.getQuantity() * existingStock.getPurchasePrice()) + totalAmount;
            double averagePrice = totalCost / totalQuantity;

            existingStock.setQuantity(totalQuantity);
            existingStock.setPurchasePrice(averagePrice);
            existingStock.setCurrentPrice(price); // Update current price with latest transaction price
            stockRepository.save(existingStock);
        } else {
            // Add new stock
            Stock newStock = new Stock();
            newStock.setSymbol(symbol);
            newStock.setName(name);
            newStock.setQuantity(quantity);
            newStock.setPurchasePrice(price);
            newStock.setCurrentPrice(price);
            newStock.setPortfolio(portfolio);
            stockRepository.save(newStock);
        }

        // Update portfolio's available cash and total value
        portfolio.setAvailableCash(portfolio.getAvailableCash() - totalAmount);
        portfolio.calculateTotalValue();
        portfolioRepository.save(portfolio);
    }

    @Transactional
    public void executeSellTransaction(String symbol, String name, double quantity, double price) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Portfolio portfolio = portfolioRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Portfolio not found"));

        // Check if user owns this stock
        Stock stock = stockRepository.findByPortfolioAndSymbol(portfolio, symbol)
                .orElseThrow(() -> new RuntimeException("Stock not found in your portfolio"));

        // Check if user has enough quantity to sell
        if (stock.getQuantity() < quantity) {
            throw new RuntimeException("Insufficient stock quantity for this transaction");
        }

        double totalAmount = quantity * price;

        // Create transaction using the factory pattern
        Transaction transaction = TransactionFactory.createSellTransaction(user, symbol, name, quantity, price);
        transactionRepository.save(transaction);

        // Update stock quantity or remove if all sold
        if (stock.getQuantity() == quantity) {
            stockRepository.delete(stock); // Remove stock completely
        } else {
            stock.setQuantity(stock.getQuantity() - quantity);
            stock.setCurrentPrice(price); // Update current price with latest transaction price
            stockRepository.save(stock);
        }

        // Update portfolio's available cash and total value
        portfolio.setAvailableCash(portfolio.getAvailableCash() + totalAmount);
        portfolio.calculateTotalValue();
        portfolioRepository.save(portfolio);
    }

    public List<Transaction> getTransactionsForCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return transactionRepository.findByUserOrderByTimestampDesc(user);
    }
}
