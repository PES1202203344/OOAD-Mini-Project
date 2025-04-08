package com.portfolio.management.config;

import org.springframework.stereotype.Component;

@Component
public class StockDataConfig {
    private static StockDataConfig instance;
    private String apiKey;
    private String apiBaseUrl;

    private StockDataConfig() {
        // Private constructor for singleton
        this.apiKey = "demo"; // Default value
        this.apiBaseUrl = "https://www.alphavantage.co/query";
    }

    public static synchronized StockDataConfig getInstance() {
        if (instance == null) {
            instance = new StockDataConfig();
        }
        return instance;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getApiBaseUrl() {
        return apiBaseUrl;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}
