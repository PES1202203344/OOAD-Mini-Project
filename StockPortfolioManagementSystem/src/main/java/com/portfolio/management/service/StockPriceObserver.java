// StockPriceObserver.java
package com.portfolio.management.service;

// Observer interface
public interface StockPriceObserver {
    void update(String symbol, double price);
}