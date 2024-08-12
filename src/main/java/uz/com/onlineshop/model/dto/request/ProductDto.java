package uz.com.onlineshop.model.dto.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProductDto {

    private String name;

    private String description;

    private Double price;

    private Integer stock;

    private String brand;

    private String model;

    private String categoryId;

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
