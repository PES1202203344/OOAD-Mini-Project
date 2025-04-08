package com.portfolio.management.controller;

import com.portfolio.management.model.Transaction;
import com.portfolio.management.service.PortfolioService;
import com.portfolio.management.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final PortfolioService portfolioService;

    @Autowired
    public TransactionController(TransactionService transactionService, PortfolioService portfolioService) {
        this.transactionService = transactionService;
        this.portfolioService = portfolioService;
    }

    @GetMapping
    public String viewTransactions(Model model) {
        model.addAttribute("transactions", transactionService.getTransactionsForCurrentUser());
        return "transaction/list";
    }

    @GetMapping("/buy")
    public String showBuyForm(Model model) {
        model.addAttribute("transaction", new Transaction());
        model.addAttribute("portfolio", portfolioService.getCurrentUserPortfolio());
        return "transaction/buy";
    }

    @PostMapping("/buy")
    public String executeBuy(@RequestParam String symbol, @RequestParam String name,
                           @RequestParam double quantity, @RequestParam double price, Model model) {
        try {
            transactionService.executeBuyTransaction(symbol, name, quantity, price);
            return "redirect:/transactions?success";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("portfolio", portfolioService.getCurrentUserPortfolio());
            return "transaction/buy";
        }
    }

    @GetMapping("/sell")
    public String showSellForm(Model model) {
        model.addAttribute("transaction", new Transaction());
        model.addAttribute("stocks", portfolioService.getAllStocksForCurrentUser());
        return "transaction/sell";
    }

    @PostMapping("/sell")
    public String executeSell(@RequestParam String symbol, @RequestParam String name,
                            @RequestParam double quantity, @RequestParam double price, Model model) {
        try {
            transactionService.executeSellTransaction(symbol, name, quantity, price);
            return "redirect:/transactions?success";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("stocks", portfolioService.getAllStocksForCurrentUser());
            return "transaction/sell";
        }
    }
}
