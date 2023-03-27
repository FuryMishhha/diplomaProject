package com.example.backend.Service;

import com.example.backend.Config.ClientConfig;
import com.example.backend.Entity.Order;
import com.example.backend.Entity.Product;
import com.example.backend.Entity.User;
import com.example.backend.Model.DataToBase;
import com.example.backend.Model.ProductSaloon;
import com.example.backend.Repository.ProductRepository;
import com.example.backend.Repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import javax.transaction.Transactional;
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
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;

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
        return result;
    }

    public void findAll() {
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

        if (result != null) {
            result.forEach(it -> {
                Product new_product = new Product(
                        it.getId(),
                        it.getSaloon_id(),
                        it.getMileage(),
                        it.getNumber_of_owners(),
                        it.getCategory(),
                        it.getBrand(),
                        it.getModel(),
                        it.getRelease_year(),
                        it.getBody(),
                        it.getColor(),
                        it.getEngine(),
                        it.getDrive(),
                        it.getWheel(),
                        it.getPrice(),
                        it.getPicture()
                );
                productRepository.save(new_product);
            });
        }

//        List <Product> product = productRepository.findAll();
//        product.sort(Comparator.comparingLong(Product::getId));
//        return product;
    }

    public String bookProduct(Long id) {
        User user = userRepository.findByUsername(String.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal()));

        findAll();

        Product bookProduct = productRepository.findById(id).orElse(null);

        if (bookProduct != null && bookProduct.getOrder_id() == null) {
            Order order = new Order();
            Product product = productRepository.findById(id).orElse(null);
            String carInfoBrand = product.getBrand();
            String carInfoModel = product.getModel();

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

            productRepository.deleteAll();

            return "redirect:/api/user/account";
        } else {
            return "redirect:/api/products";
        }
    }

    public Product findConcreteProduct(Long id) {
        return productRepository.findById(id).orElse(null);
    }

//    public Product deleteProduct(Long id) {
//        Product product = productRepository.findById(id).orElse(null);
//        if (product != null) {
//            if (product.getOrder_id() != null) {
//                orderService.deleteOrder(product.getOrder_id());
//            }
//            productRepository.deleteById(id);
//            return product;
//        } else return null;
//    }

    public String parse(String url) {
        if (url.charAt(("https://drive.google.com/").length()) == 'u') return url;
        return "https://drive.google.com/uc?export=view&id=" + url.substring(("https://drive.google.com/file/d/").length(), url.length() - "/view?usp=sharing".length());
    }
}
