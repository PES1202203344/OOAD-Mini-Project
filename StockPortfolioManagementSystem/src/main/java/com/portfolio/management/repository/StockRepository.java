package com.portfolio.management.repository;

import com.portfolio.management.model.Portfolio;
import com.portfolio.management.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    List<Stock> findByPortfolio(Portfolio portfolio);
    Optional<Stock> findByPortfolioAndSymbol(Portfolio portfolio, String symbol);
}
