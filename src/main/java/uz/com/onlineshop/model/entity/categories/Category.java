package uz.com.onlineshop.model.entity.categories;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;
import uz.com.onlineshop.model.entity.BaseEntity;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity(name = "categories")
public class Category extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String description;
}
