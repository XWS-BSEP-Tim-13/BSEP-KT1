package com.example.backend.repository;

import com.example.backend.model.ForgotPasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForgotPasswordTokenRepository extends JpaRepository<ForgotPasswordToken,Integer> {

    ForgotPasswordToken findByToken(String token);
}
