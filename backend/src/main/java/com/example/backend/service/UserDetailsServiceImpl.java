package com.example.backend.service;

import com.example.backend.model.CertificationEntity;
import com.example.backend.repository.CertificationEntityRepository;
import com.example.backend.service.interfaces.UserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private  final CertificationEntityRepository certificationEntityRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        CertificationEntity certificationEntity=certificationEntityRepository.findByEmail(email);
        if (certificationEntity == null) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", email));
        } else {
            return certificationEntity;
        }
    }
}
