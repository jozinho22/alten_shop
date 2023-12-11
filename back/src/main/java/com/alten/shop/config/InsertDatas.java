package com.alten.shop.config;

import com.alten.shop.model.Product;
import com.alten.shop.model.security.Role;
import com.alten.shop.model.security.AuthorizedUser;
import com.alten.shop.repository.ProductRepository;
import com.alten.shop.repository.security.AuthorizedUserRepository;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Configuration
public class InsertDatas {

    @Autowired
    private AuthorizedUserRepository uRepo;
    @Autowired
    private ProductRepository prodRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("classpath:products.json")
    Resource sample;

    @EventListener(ApplicationReadyEvent.class)
    public void insert() throws IOException {

        System.out.println("Inserting the datas :");

        AuthorizedUser admin = new AuthorizedUser("joss@gmail.com", passwordEncoder.encode("joss"), List.of(Role.ADMIN));
        uRepo.save(admin);

        File sampleFile = sample.getFile();

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true);

        List<Product> products = mapper.reader().forType(new TypeReference<List<Product>>() {
        }).readValue(sampleFile);

        prodRepo.saveAll(products);

        System.out.println("------------------");
        System.out.println("Datas inserted ");
        System.out.println("------------------");
    }
}