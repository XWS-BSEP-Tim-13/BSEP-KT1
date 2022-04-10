package com.example.backend.service;

import com.example.backend.model.OrganizationKeystoreAccess;
import com.example.backend.service.interfaces.KeystorePasswordsService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class KeystorePasswordsServiceImpl implements KeystorePasswordsService {

    @Autowired
    private Environment environment;
    String path;

    Gson gson=new Gson();

    public KeystorePasswordsServiceImpl(Environment env) {
        this.environment = env;
        path=environment.getProperty("keystore.passwords");
    }

    @Override
    public List<OrganizationKeystoreAccess> getAll() {
        Reader reader = null;
        try {
            reader = Files.newBufferedReader(Paths.get(path));
            List<OrganizationKeystoreAccess> ret= gson.fromJson(reader, new TypeToken<List<OrganizationKeystoreAccess>>() {}.getType());
            if(ret == null)
                return new ArrayList<OrganizationKeystoreAccess>();
            return ret;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String findPasswordByOrganization(String organization){
        List<OrganizationKeystoreAccess> list = getAll();
        for(OrganizationKeystoreAccess item : list){
            if(item.getOrganiation().equals(organization))
                return item.getPassword();
        }
        return "";
    }


    @Override
    public boolean save(OrganizationKeystoreAccess oksa) {
        try {
            List<OrganizationKeystoreAccess> list = getAll();
            list.add(oksa);
            FileWriter fileWriter=new FileWriter(path);
            String json=gson.toJson(list);
            fileWriter.write(json);
            fileWriter.flush();
            fileWriter.close();
            return true;
        } catch (IOException e) {
           return false;
        }
    }
}
