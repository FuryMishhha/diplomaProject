package com.example.backend.Controller;

import com.example.backend.Entity.Order;
import com.example.backend.Entity.User;
import com.example.backend.Model.ProductSaloon;
import com.example.backend.Service.OrderService;
import com.example.backend.Service.ProductService;
import com.example.backend.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @GetMapping("/users")
    public List<User> allUsers() {
        return userService.allUsers();
    }

    @GetMapping("/users/{id}")
    public User showConcreteUser(@PathVariable Long id) {
        return userService.findConcreteUser(id);
    }

    @PutMapping("/users/{id}")
    void editUser(@PathVariable Long id, @RequestBody User user) {
        userService.updateUser(id, user);
    }

    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }

    @GetMapping("/products")
    public List<ProductSaloon> allProducts() {
        return productService.findAllList();
    }

    @GetMapping("/products/{id}")
    public ProductSaloon showConcreteProduct(@PathVariable Long id) {
        return productService.findConcreteProduct(id);
    }

//    @DeleteMapping("/products/{id}")
//    public String deleteProduct(@RequestBody DataToBase dataToBase) {
//        return productService.deleteProduct(dataToBase.getOrder_id());
//    }

    @GetMapping("/orders")
    public List<Order> allOrders() {
        return orderService.allOrders();
    }

    @GetMapping("/orders/{id}")
    public Order showConcreteOrder(@PathVariable Integer id) {
        return orderService.findConcreteOrder(id);
    }

    @DeleteMapping("/orders/{id}")
    public String deleteOrder(@PathVariable Integer id) {
        return orderService.deleteOrder(id);
    }

    @PutMapping("/orders/{id}")
    public void updateOrderInfo(@PathVariable Integer id, @RequestBody Order order) {
        orderService.updateOrder(id, order);
    }
}
