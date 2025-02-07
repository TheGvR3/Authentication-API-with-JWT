package com.thcsOauth2.AutenticatorDemo.repository;

import com.thcsOauth2.AutenticatorDemo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
} 