package com.portfolio.management.service;

import com.portfolio.management.model.Transaction;
import com.portfolio.management.model.User;
import java.time.LocalDateTime;

// Factory Pattern implementation for creating transactions
public class TransactionFactory {

    public static Transaction createBuyTransaction(User user, String symbol, String name, double quantity, double price) {
        Transaction transaction = new Transaction();
        transaction.setType(Transaction.TransactionType.BUY);
        transaction.setStockSymbol(symbol);
        transaction.setStockName(name);
        transaction.setQuantity(quantity);
        transaction.setPrice(price);
        transaction.setTotalAmount(quantity * price);
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setUser(user);
        return transaction;
    }

    public static Transaction createSellTransaction(User user, String symbol, String name, double quantity, double price) {
        Transaction transaction = new Transaction();
        transaction.setType(Transaction.TransactionType.SELL);
        transaction.setStockSymbol(symbol);
        transaction.setStockName(name);
        transaction.setQuantity(quantity);
        transaction.setPrice(price);
        transaction.setTotalAmount(quantity * price);
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setUser(user);
        return transaction;
    }
}
