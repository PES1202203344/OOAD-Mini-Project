// PortfolioAnalysisStrategy.java
package com.portfolio.management.service;

import com.portfolio.management.model.Portfolio;

// Strategy Pattern interface
public interface PortfolioAnalysisStrategy {
    double analyze(Portfolio portfolio);
}