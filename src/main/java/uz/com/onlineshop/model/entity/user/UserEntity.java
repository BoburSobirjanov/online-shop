package uz.com.onlineshop.model.entity.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import uz.com.onlineshop.model.entity.BaseEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;


@Entity(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserEntity extends BaseEntity implements UserDetails {

    @Column(nullable = false)
    @Size(min = 2, message = "Full name must be at least 2 characters long")
    @NotBlank(message = "Full name is required")
    private String fullName;

    @Column(nullable = false)
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[!@#$%^&*]).+$",
            message = "Password must contain at least one uppercase letter and one special character")
    @NotBlank(message = "Password is required")
    private String password;


    @Column(nullable = false, unique = true)
    @Pattern(regexp = "^\\+998\\d{9}$", message = "Phone number must be in the format +998XXXXXXXXX")
    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@gmail\\.com$", message = "Email should be in the format ....@gmail.com")
    @NotBlank(message = "Email is required")
    private String email;

    @Column(nullable = false)
    @NotBlank(message = "Address is required")
    private String address;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    private UUID blockedBy;

    private UUID activeBy;

    private UUID changeRoleBy;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role.name()));
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }


    public String getRealUsername(){return username;}
}
