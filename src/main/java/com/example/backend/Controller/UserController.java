package com.example.backend.Controller;

import com.example.backend.Model.ProfileResponse;
import com.example.backend.Service.ProductService;
import com.example.backend.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    ProductService productService;

    @GetMapping("/profile")
    public ProfileResponse getUserInfo(){
        return userService.getProfile();
    }

    @PostMapping("/products/{id}")
    public String bookProduct(@PathVariable Long id){
        return productService.bookProduct(id);
    }
}
