package com.alten.shop.controller;

import com.alten.shop.model.Product;
import com.alten.shop.repository.ProductRepository;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import jakarta.validation.Valid;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin
public class ProductController {

    private final ProductRepository prodRepo;
    @Autowired
    public ProductController(ProductRepository prodRepo) {
        this.prodRepo = prodRepo;
    }

    @Value("classpath:products.json")
    Resource sample;

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public List<Product> findAll() {
        return prodRepo.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Product findById(@PathVariable Long id) {
        return prodRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Content not existing in db"
                        )
                );
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> create(@Valid @RequestBody Product p, Errors errors) {
        if (errors.hasErrors()) {
            return new ResponseEntity(errors.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        prodRepo.save(p);

        return ResponseEntity.ok("Le produit n°" + p.getId() + " a bien été créé");
    }


    // insert datas
    /*@PostMapping("/sample")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> createDatas() throws IOException {

        File sampleFile = sample.getFile();

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true);

        List<Product> products = mapper.reader().forType(new TypeReference<List<Product>>() {
        }).readValue(sampleFile);

        prodRepo.saveAll(products);

        return ResponseEntity.ok("Les " + products.size() + " produits ont bien été créés");
    }*/

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Product> patch(@PathVariable Long id, @Valid @RequestBody Product pDetails, Errors errors) {
        if (errors.hasErrors()) {
            return new ResponseEntity(errors.getAllErrors(), HttpStatus.BAD_REQUEST);
        }

        Product p = prodRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found for the id : " + id));

        prodRepo.save(pDetails);
        return ResponseEntity.ok(pDetails);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<String> delete(@PathVariable Long id) {
        Product p = prodRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found for the id : " + id));

        prodRepo.delete(p);
        return ResponseEntity.ok("Le produit n°" + id + " a bien été supprimé" );
    }


}
