package com.portfolio.management.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;
    private String password;
    private boolean active = true;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Portfolio portfolio;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Transaction> transactions = new ArrayList<>();
}
