package com.example.backend.Service;

import com.example.backend.Config.ClientConfig;
import com.example.backend.Entity.Order;
import com.example.backend.Entity.Product;
import com.example.backend.Model.DataToBase;
import com.example.backend.Model.ProductSaloon;
import com.example.backend.Repository.OrderRepository;
import com.example.backend.Repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@Transactional
public class OrderService {
    @Autowired
    private ProductService productService;
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    public Order addOrder(Order order) {
        return orderRepository.save(order);
    }

    public List<Order> allOrders() {
        List<Order> order = orderRepository.findAll();
        order.sort(Comparator.comparingInt(Order::getId));
        return order;
    }

    public Order updateOrder(Integer id, Order order) {
        Order updatedOrder = findConcreteOrder(id);
        if (updatedOrder != null) {
            if (!order.getStatus().equals("")) updatedOrder.setStatus(order.getStatus());
            return orderRepository.save(updatedOrder);
        } else return null;
    }

    public Order deleteOrder(Integer id) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order != null) {
            Long productOriginalId = null;
            List<ProductSaloon> products = productService.findAllList();
            for (ProductSaloon product : products) {
                if (product.getOrder_id().equals(id)) {
                    productOriginalId = product.getId();
                }
            }
            ProductSaloon productSaloon =

                    Product product = productRepository.findProductByOrder_id(id);

            if (order != null && product != null) {
                String uri = "/api" + order.getSaloon_id() + "/productsbook/" + order.getOriginal_id();
                DataToBase dataToBase = new DataToBase(
                        order.getOriginal_id(),
                        null
                );

                WebClient webClient = ClientConfig.webClientWithTimeout(order.getSaloon_id());
                webClient
                        .put()
                        .uri(uri)
                        .syncBody(dataToBase)
                        .retrieve()
                        .bodyToMono(DataToBase.class)
                        .block();

                productRepository.deleteById(product.getId());
                orderRepository.deleteById(id);
                return order;
            } else return null;
        }
    }

    public Order findConcreteOrder(Integer id) {
        return orderRepository.findOrderById(id);
    }
}

