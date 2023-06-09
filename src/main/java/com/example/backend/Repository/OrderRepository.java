package com.example.backend.Repository;

import com.example.backend.Entity.Order;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Qualifier("baseDataSource")
public interface OrderRepository extends JpaRepository <Order, Integer> {
    Long deleteOrderById(Integer id);
    Order findOrderById(Integer id);
    List<Order> findByUserId(Long userId);
}