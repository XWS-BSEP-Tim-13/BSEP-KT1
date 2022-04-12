package com.example.backend.config;

import com.example.backend.dto.CertificateDto;
import com.example.backend.model.Certificate;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        TypeMap<Certificate, CertificateDto> certificateMap = modelMapper.createTypeMap(Certificate.class,CertificateDto.class);
        certificateMap.addMapping(src->src.getSubject().getCommonName(),CertificateDto::setSubject)
                .addMapping(src->src.getParentCertificate().getSubject().getCommonName(),CertificateDto::setIssuer)
                .addMapping(src->src.getId(),CertificateDto::setSerialNumber);

        return modelMapper;
    }
}

