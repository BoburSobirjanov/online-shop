package uz.com.onlineshop.model.dto.response;

import lombok.*;
import uz.com.onlineshop.model.entity.user.Gender;
import uz.com.onlineshop.model.entity.user.UserRole;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserForFront {

    private UUID id;

    private String fullName;

    private String phoneNumber;

    private String username;

    private String email;

    private String address;

    private UserRole role;

    private Gender gender;
}
