package com.portfolio.management.controller;

import com.portfolio.management.model.Portfolio;
import com.portfolio.management.model.Stock;
import com.portfolio.management.service.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/portfolio")
public class PortfolioController {

    private final PortfolioService portfolioService;

    @Autowired
    public PortfolioController(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

    @GetMapping
    public String viewPortfolio(Model model) {
        Portfolio portfolio = portfolioService.getCurrentUserPortfolio();
        model.addAttribute("portfolio", portfolio);
        model.addAttribute("stocks", portfolioService.getAllStocksForCurrentUser());
        return "portfolio/view";
    }

    @GetMapping("/add")
    public String showAddStockForm(Model model) {
        model.addAttribute("stock", new Stock());
        return "portfolio/add-stock";
    }

    @PostMapping("/add")
    public String addStock(@ModelAttribute Stock stock, Model model) {
        try {
            portfolioService.addStock(stock);
            return "redirect:/portfolio?success";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "portfolio/add-stock";
        }
    }

    @GetMapping("/remove/{id}")
    public String removeStock(@PathVariable Long id) {
        try {
            portfolioService.removeStock(id);
            return "redirect:/portfolio?removed";
        } catch (Exception e) {
            return "redirect:/portfolio?error=" + e.getMessage();
        }
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        try {
            Stock stock = portfolioService.getStockById(id);
            model.addAttribute("stock", stock);
            return "portfolio/update-stock";
        } catch (Exception e) {
            return "redirect:/portfolio?error=" + e.getMessage();
        }
    }

    @PostMapping("/update")
    public String updateStock(@ModelAttribute Stock stock, Model model) {
        try {
            portfolioService.updateStock(stock);
            return "redirect:/portfolio?updated";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("stock", stock);
            return "portfolio/update-stock";
        }
    }
}
