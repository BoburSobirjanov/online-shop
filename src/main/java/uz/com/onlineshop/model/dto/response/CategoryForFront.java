package uz.com.onlineshop.model.dto.response;

import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CategoryForFront {

    private UUID id;

    private String name;

    private String description;
}
