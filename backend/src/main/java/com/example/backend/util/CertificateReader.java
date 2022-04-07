package com.example.backend.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.List;

public class CertificateReader {

    public List<Certificate> getAllCertificates(String filePath) {
        try {
            FileInputStream fis = new FileInputStream(filePath);
            BufferedInputStream bis = new BufferedInputStream(fis);

            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            List<Certificate> certificates = new ArrayList<>();
            //Cita sertifikat po sertifikat
            //Svaki certifikat je izmedju
            //-----BEGIN CERTIFICATE-----,
            //i
            //-----END CERTIFICATE-----.
            while (bis.available() > 0) {
               certificates.add(cf.generateCertificate(bis));
            }
            return certificates;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
