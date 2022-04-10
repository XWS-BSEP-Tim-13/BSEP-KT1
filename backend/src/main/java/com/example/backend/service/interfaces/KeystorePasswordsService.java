package com.example.backend.service.interfaces;

import com.example.backend.model.OrganizationKeystoreAccess;

import java.util.List;

public interface KeystorePasswordsService {

    List<OrganizationKeystoreAccess> getAll();
    boolean save(OrganizationKeystoreAccess oksa);
    String findPasswordByOrganization(String organization);
}
