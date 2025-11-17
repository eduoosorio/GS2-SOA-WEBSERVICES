package com.futurework.hub.repository;

import com.futurework.hub.domain.user.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
    Optional<UserAccount> findByEmail(String email);

    Optional<UserAccount> findByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);
}

