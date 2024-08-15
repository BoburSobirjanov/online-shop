package uz.com.onlineshop.model.entity.apicount;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity(name = "api_request_count")
public class ApiRequestCount {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String apiEndpoint;

    private Integer requestCount;

    public ApiRequestCount(String apiEndpoint, Integer requestCount) {
        this.apiEndpoint = apiEndpoint;
        this.requestCount = requestCount;
    }
}
