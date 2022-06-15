package com.example.backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import java.util.Date;

@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
@Entity
public class PasswordlessCredentials extends BaseEntity {
    private String email;
    private String code;
    private Date expiringDate;
}
