package uz.com.onlineshop.model.dto.response;

import lombok.*;
import uz.com.onlineshop.model.enums.Gender;
import uz.com.onlineshop.model.enums.UserRole;

import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserForFrontDto {

    private UUID id;

    private String fullName;

    private String phoneNumber;

    private String username;

    private String email;

    private String address;

    private Set<UserRole> role;

    private Gender gender;
}
