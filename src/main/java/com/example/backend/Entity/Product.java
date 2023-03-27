package com.example.backend.Entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "products")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Product implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long original_id;
    private Integer saloon_id;
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

    public Product(Long original_id, Integer saloon_id, Integer mileage, Integer number_of_owners, String category, String brand, String model, Integer release_year, String body, String color, String engine, String drive, String wheel, Integer price, String picture) {
        this.original_id = original_id;
        this.saloon_id = saloon_id;
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
    }
}
