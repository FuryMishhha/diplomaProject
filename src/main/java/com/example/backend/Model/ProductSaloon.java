package com.example.backend.Model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductSaloon implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer mileage;
    private Integer number_of_owners;
    private String category;
    private String brand;
    private String model;
    private Integer release_year;
    private String body;
    private String color;
    private String engine;
    private String drive;
    private String wheel;
    private Integer price;
    private String picture;
    private Integer order_id;
    private Integer saloon_id;
    private Long original_id;

    public ProductSaloon(Integer mileage, Integer number_of_owners, String category, String brand, String model, Integer release_year, String body, String color, String engine, String drive, String wheel, Integer price, String picture, Integer saloon_id) {
        this.mileage = mileage;
        this.number_of_owners = number_of_owners;
        this.category = category;
        this.brand = brand;
        this.model = model;
        this.release_year = release_year;
        this.body = body;
        this.color = color;
        this.engine = engine;
        this.drive = drive;
        this.wheel = wheel;
        this.price = price;
        this.picture = picture;
        this.saloon_id = saloon_id;
    }
}
