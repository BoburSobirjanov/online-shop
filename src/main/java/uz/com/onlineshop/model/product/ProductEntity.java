package uz.com.onlineshop.model.product;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;
import uz.com.onlineshop.model.BaseEntity;
import uz.com.onlineshop.model.categories.Category;

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

    @ManyToOne
    private Category categoryId;

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

}
