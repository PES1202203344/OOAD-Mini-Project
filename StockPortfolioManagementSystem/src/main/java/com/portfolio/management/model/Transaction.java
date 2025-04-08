package com.portfolio.management.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    private String stockSymbol;
    private String stockName;
    private double quantity;
    private double price;
    private double totalAmount;
    private LocalDateTime timestamp;

    @ManyToOne
    private User user;

    public enum TransactionType {
        BUY, SELL
    }
}
