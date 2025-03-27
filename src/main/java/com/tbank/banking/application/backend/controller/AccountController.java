package com.tbank.banking.application.backend.controller;

import com.tbank.banking.application.backend.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/tbank/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(@RequestBody Map<String, Object> request) {
        Long userId = ((Number) request.get("userId")).longValue();
        long amount = ((Number) request.get("amount")).longValue();

        if (accountService.deposit(userId, amount)) {
            return ResponseEntity.ok("Deposit successful.");
        }
        return ResponseEntity.badRequest().body("Failed to deposit.");
    }

    @PostMapping("/withdraw")
    public ResponseEntity<String> withdraw(@RequestBody Map<String, Object> request) {
        Long userId = ((Number) request.get("userId")).longValue();
        long amount = ((Number) request.get("amount")).longValue();

        if (accountService.withdraw(userId, amount)) {
            return ResponseEntity.ok("Withdrawal successful.");
        }
        return ResponseEntity.badRequest().body("Insufficient balance or account not found.");
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(@RequestBody Map<String, Object> request) {
        Long senderId = ((Number) request.get("senderId")).longValue();
        Long receiverId = ((Number) request.get("receiverId")).longValue();
        long amount = ((Number) request.get("amount")).longValue();

        if (accountService.transferMoney(senderId, receiverId, amount)) {
            return ResponseEntity.ok("Transfer successful.");
        }
        return ResponseEntity.badRequest().body("Transfer failed. Check balance and account details.");
    }
}