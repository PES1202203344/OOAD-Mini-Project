package com.portfolio.management.service;

import com.portfolio.management.model.Portfolio;
import com.portfolio.management.model.User;
import com.portfolio.management.repository.PortfolioRepository;
import com.portfolio.management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PortfolioRepository portfolioRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PortfolioRepository portfolioRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.portfolioRepository = portfolioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User registerUser(User user) {
        // Check if user already exists
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already in use");
        }
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already taken");
        }

        // Encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Save user
        User savedUser = userRepository.save(user);

        // Create portfolio for new user
        Portfolio portfolio = new Portfolio();
        portfolio.setUser(savedUser);
        portfolio.setTotalValue(portfolio.getAvailableCash()); // Initial value is just available cash
        portfolioRepository.save(portfolio);

        return savedUser;
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
