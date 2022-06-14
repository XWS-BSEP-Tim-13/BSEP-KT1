package com.example.backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import java.time.LocalDateTime;
import java.util.Date;


@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
@Entity
public class ForgotPasswordToken extends BaseEntity{

    public String token;
    public String email;
    public LocalDateTime expiringDate;
}
