package uz.com.onlineshop.model.dto.request.user;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserDto {

    private String fullName;
    private String email;
    private String phoneNumber;
    private String address;
    private String password;
    private String gender;
    private String username;
}
