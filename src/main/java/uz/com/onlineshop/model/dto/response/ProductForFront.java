package uz.com.onlineshop.model.dto.response;


import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProductForFront {

    private UUID id;

    private String name;

    private String description;

    private Double price;

    private Integer stock;

    private String brand;

    private String model;

    private UUID categoryId;

    private Double weight;

    private String dimensions;

    private String color;

    private String material;

    private String warranty;

    private String sku;

    private String barcode;

    private String manufacturer;

    private String countryOfOrigin;

}
