package com.tbank.banking.application.backend.repos;

import com.tbank.banking.application.backend.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface AccountRepo extends JpaRepository<Account, Long> {
    Optional<Account> findByUserId(Long userId);
}
