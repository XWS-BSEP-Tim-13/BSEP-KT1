package com.example.backend.model;

import lombok.*;

import javax.persistence.Entity;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerificationData extends BaseEntity {

    private String code;
    private boolean codeUsed;
    private String email;
    private Date expiresAt;

}
