package com.securetech;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Arrays;
import java.util.List;

@RestController
public class ProductController {

    @GetMapping("/products")
    public List<Product> getProducts() {
        return Arrays.asList(
            new Product(1, "Laptop", 999.99),
            new Product(2, "Souris", 29.99),
            new Product(3, "Clavier", 49.99)
        );
    }

    @GetMapping("/health")
    public String health() {
        return "OK - product-api-devsecops is running";
    }
}