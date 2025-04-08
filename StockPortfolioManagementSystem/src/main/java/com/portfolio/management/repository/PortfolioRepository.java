package com.portfolio.management.repository;

import com.portfolio.management.model.Portfolio;
import com.portfolio.management.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
    Optional<Portfolio> findByUser(User user);
}
