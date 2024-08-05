package uz.com.onlineshop.response;

import lombok.*;
import uz.com.onlineshop.model.dto.response.UserForFront;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class JwtResponse {

    private String accessToken;

    private String refreshToken;

    private UserForFront userForFront;
}
