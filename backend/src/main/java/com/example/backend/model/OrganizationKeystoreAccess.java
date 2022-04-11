package com.example.backend.model;

import lombok.*;

@AllArgsConstructor
@Data
@Builder
public class OrganizationKeystoreAccess {

    private String password;
    private String organiation;
}
