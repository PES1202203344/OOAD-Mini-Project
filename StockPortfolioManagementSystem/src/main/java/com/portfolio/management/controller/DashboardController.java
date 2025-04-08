package com.portfolio.management.controller;

import com.portfolio.management.model.Portfolio;
import com.portfolio.management.service.PerformanceAnalysisStrategy;
import com.portfolio.management.service.PortfolioService;
import com.portfolio.management.service.RiskAnalysisStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final PortfolioService portfolioService;

    @Autowired
    public DashboardController(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

    @GetMapping({"/", "/dashboard"})
    public String dashboard(Model model) {
        Portfolio portfolio = portfolioService.getCurrentUserPortfolio();
        
        // Get performance analysis
        portfolioService.setAnalysisStrategy(new PerformanceAnalysisStrategy());
        double performance = portfolioService.analyzePortfolio(portfolio);
        
        // Get risk analysis
        portfolioService.setAnalysisStrategy(new RiskAnalysisStrategy());
        double risk = portfolioService.analyzePortfolio(portfolio);
        
        model.addAttribute("portfolio", portfolio);
        model.addAttribute("performance", performance);
        model.addAttribute("risk", risk);
        model.addAttribute("stocks", portfolioService.getAllStocksForCurrentUser());
        
        return "dashboard/index";
    }
}
