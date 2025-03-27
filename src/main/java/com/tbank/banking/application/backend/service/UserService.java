package com.tbank.banking.application.backend.service;

import com.tbank.banking.application.backend.models.Account;
import com.tbank.banking.application.backend.models.User;
import com.tbank.banking.application.backend.repos.AccountRepo;
import com.tbank.banking.application.backend.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepository;
    @Autowired
    private AccountRepo accountRepo;

    public User registerUser(User user) {
        User savedUser = userRepository.save(user);

        // Automatically create an account
        Account account = new Account();
        account.setUser(savedUser);
        account.setBalance(0L); // Default balance
        accountRepo.save(account);

        return savedUser;
    }
    @RateLimiter(name="default",fallbackMethod = "rateLimitFallback")
    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    public Optional<User> rateLimitFallback(Long userId, Throwable t) {
        return Optional.empty();
    }
}
