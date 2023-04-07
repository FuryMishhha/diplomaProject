package com.example.backend.Service;

import com.example.backend.Config.ClientConfig;
import com.example.backend.Entity.Order;
import com.example.backend.Entity.User;
import com.example.backend.Model.DataToBase;
import com.example.backend.Model.ProductSaloon;
import com.example.backend.Repository.OrderRepository;
import com.example.backend.Repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@Transactional
public class ProductService {
    @Autowired
    OrderService orderService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;

    public List<ProductSaloon> findAllList() {
        WebClient webClient = ClientConfig.webClientWithTimeout(1);
        List<ProductSaloon> result = webClient
                .get()
                .uri("/api1/products")
                .retrieve()
                .toEntityList(ProductSaloon.class)
                .block()
                .getBody();

//        WebClient webClient2 = ClientConfig.webClientWithTimeout(2);
//        List<Product> second = webClient2
//                .get()
//                .uri("/api2/products")
//                .retrieve()
//                .toEntityList(Product.class)
//                .block()
//                .getBody();
//
//        result.addAll(second);
//
//        WebClient webClient3 = ClientConfig.webClientWithTimeout(3);
//        List<Product> third = webClient3
//                .get()
//                .uri("/api3/products")
//                .retrieve()
//                .toEntityList(Product.class)
//                .block()
//                .getBody();
//
//        result.addAll(third);

        for (int i = 0; i < result.size(); i++) {
            result.get(i).setId((long) i);
        }

        return result;
    }

    public String bookProduct(Long id) {
        User user = userRepository.findByUsername(String.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal()));
        List<ProductSaloon> products = findAllList();
        ProductSaloon bookProduct = products.get(id.intValue());

        if (bookProduct != null && bookProduct.getOrder_id() == null) {
            Order order = new Order();
            String carInfoBrand = bookProduct.getBrand();
            String carInfoModel = bookProduct.getModel();

            Integer saloon_id = bookProduct.getSaloon_id();
            Long original_id = bookProduct.getOriginal_id();
            order.setOriginal_id(original_id);
            order.setSaloon_id(saloon_id);
            order.setCarInfo(carInfoBrand + " " + carInfoModel);
            order.setUserId(user.getId());
            order.setStatus("Резерв");

            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date date = new Date();
            order.setCreation_date(dateFormat.format(date));

            orderService.addOrder(order);

            String uri = "/api" + saloon_id + "/productsbook/" + original_id;

            DataToBase dataToBase = new DataToBase(
                    bookProduct.getId(),
                    order.getId()
            );

            WebClient webClient = ClientConfig.webClientWithTimeout(saloon_id);
            webClient
                    .put()
                    .uri(uri)
                    .syncBody(dataToBase)
                    .retrieve()
                    .bodyToMono(DataToBase.class)
                    .block();

            return "redirect:/api/user/account";
        } else {
            return "redirect:/api/products";
        }
    }

//    public String deleteProduct(Integer id) {
//        Order order = orderRepository.findById(id).orElse(null);
//        if (order != null)
//            orderRepository.deleteById(id);
//        return "redirect:/api/admin/orders";
//    }

    public ProductSaloon findConcreteProduct(Long id) {
        return findAllList().get(id.intValue());
    }

    public String parse(String url) {
        if (url.charAt(("https://drive.google.com/").length()) == 'u') return url;
        return "https://drive.google.com/uc?export=view&id=" + url.substring(("https://drive.google.com/file/d/").length(), url.length() - "/view?usp=sharing".length());
    }
}
