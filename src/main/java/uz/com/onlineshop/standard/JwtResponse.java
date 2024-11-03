package uz.com.onlineshop.standard;

import lombok.*;
import uz.com.onlineshop.model.dto.response.UserForFrontDto;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class JwtResponse {

    private String accessToken;

    private String refreshToken;

    private UserForFrontDto userForFrontDto;
}
