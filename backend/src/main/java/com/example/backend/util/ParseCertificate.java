package com.example.backend.util;

import com.example.backend.model.CertificationEntity;
import org.springframework.stereotype.Component;

import javax.security.auth.x500.X500Principal;

@Component
public class ParseCertificate {
    public CertificationEntity parseX500Principal(X500Principal principal){
        CertificationEntity entity = new CertificationEntity();
        String rawData = principal.toString();
        String[] splits = rawData.split(",");
        for(String split : splits){
            String[] pair = split.split("=");
            String firstToken = pair[0].strip();
            switch (firstToken){
                case "UID":
                    entity.setId(Integer.parseInt(pair[1]));
                    break;
                case "EMAILADDRESS":
                    entity.setEmail(pair[1]);
                    break;
                case "C":
                    entity.setCountryCode(pair[1]);
                    break;
                case "O":
                    entity.setOrganization(pair[1]);
                    break;
                case "CN":
                    entity.setCommonName(pair[1]);
                    break;
                default:
                    break;

            }
        }
        return entity;
    }
}
