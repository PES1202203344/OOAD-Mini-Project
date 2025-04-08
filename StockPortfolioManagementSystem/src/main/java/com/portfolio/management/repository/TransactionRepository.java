package com.portfolio.management.repository;

import com.portfolio.management.model.Transaction;
import com.portfolio.management.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUser(User user);
    List<Transaction> findByUserOrderByTimestampDesc(User user);
}
