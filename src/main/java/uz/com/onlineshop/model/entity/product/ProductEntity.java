package uz.com.onlineshop.model.entity.product;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import lombok.*;
import uz.com.onlineshop.model.entity.BaseEntity;


import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity(name = "products")
public class ProductEntity extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Integer stock;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private String model;

    private UUID categoryId;

    @Column(nullable = false)
    private Double weight;

    @Column(nullable = false)
    private String dimensions;

    @Column(nullable = false)
    private String color;

    @Column(nullable = false)
    private String material;

    @Column(nullable = false)
    private String warranty;

    @Column(nullable = false)
    private String sku;

    @Column(nullable = false)
    private String barcode;

    @Column(nullable = false)
    private String manufacturer;

    @Column(nullable = false)
    private String countryOfOrigin;

    private Integer viewCount = 0;

    @Column(columnDefinition = "integer default 0")
    private Integer isSale;

}
