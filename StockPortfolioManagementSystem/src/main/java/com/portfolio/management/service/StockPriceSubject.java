// StockPriceSubject.java
package com.portfolio.management.service;

import java.util.ArrayList;
import java.util.List;

// Observer Pattern implementation
public class StockPriceSubject {
    private final List<StockPriceObserver> observers = new ArrayList<>();
    private double price;
    private String symbol;

    public void attach(StockPriceObserver observer) {
        observers.add(observer);
    }

    public void detach(StockPriceObserver observer) {
        observers.remove(observer);
    }

    public void notifyAllObservers() {
        for (StockPriceObserver observer : observers) {
            observer.update(symbol, price);
        }
    }

    public void setPriceForSymbol(String symbol, double price) {
        this.symbol = symbol;
        this.price = price;
        notifyAllObservers();
    }
}

