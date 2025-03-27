package com.tbank.banking.application.backend.service;

import com.tbank.banking.application.backend.models.Account;
import com.tbank.banking.application.backend.repos.AccountRepo;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepo accountRepository;

    @Autowired
    private TransactionService transactionService;

    public Optional<Account> getAccountByUserId(Long userId) {
        return accountRepository.findByUserId(userId);
    }

    public void saveAccount(Account account) {
        accountRepository.save(account);
    }

    public boolean deposit(Long userId, long amount) {
        Optional<Account> accountOpt = accountRepository.findByUserId(userId);
        if (accountOpt.isPresent()) {
            Account account = accountOpt.get();
            account.setBalance(account.getBalance() + amount);
            accountRepository.save(account);
            transactionService.recordTransaction(account, amount, "DEPOSIT");
            return true;
        }
        return false;
    }

    public boolean withdraw(Long userId, long amount) {
        Optional<Account> accountOpt = accountRepository.findByUserId(userId);
        if (accountOpt.isPresent()) {
            Account account = accountOpt.get();
            if (account.getBalance() >= amount) {
                account.setBalance(account.getBalance() - amount);
                accountRepository.save(account);
                transactionService.recordTransaction(account, amount, "WITHDRAW");
                return true;
            }
        }
        return false;
    }

    @Transactional
    @RateLimiter(name = "default", fallbackMethod = "rateLimitFallback")
    public boolean transferMoney(Long senderId, Long receiverId, long amount) {
        Optional<Account> senderOpt = accountRepository.findByUserId(senderId);
        Optional<Account> receiverOpt = accountRepository.findByUserId(receiverId);

        if (senderOpt.isPresent() && receiverOpt.isPresent()) {
            Account sender = senderOpt.get();
            Account receiver = receiverOpt.get();

            if (sender.getBalance() < amount) {
                return false; // Insufficient funds
            }

            sender.setBalance(sender.getBalance() - amount);
            accountRepository.save(sender);

            receiver.setBalance(receiver.getBalance() + amount);
            accountRepository.save(receiver);

            transactionService.recordTransaction(sender, amount, "TRANSFER_OUT");
            transactionService.recordTransaction(receiver, amount, "TRANSFER_IN");

            return true;
        }
        return false;
    }
}