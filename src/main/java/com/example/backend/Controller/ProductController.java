package com.example.backend.Controller;

import com.example.backend.Entity.Product;
import com.example.backend.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping("/api/products")
    public void getAllProducts(){
        productService.findAll();
    }

    @GetMapping("/api/products/{id}")
    public Product showConcreteProduct(@PathVariable Long id){
        return productService.findConcreteProduct(id);
    }
}



