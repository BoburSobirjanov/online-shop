package uz.com.onlineshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.com.onlineshop.model.dto.request.user.UserDto;
import uz.com.onlineshop.model.dto.response.UserForFront;
import uz.com.onlineshop.model.entity.user.UserEntity;
import uz.com.onlineshop.response.StandardResponse;
import uz.com.onlineshop.service.UserService;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {




    private final UserService userService;
    @GetMapping("/get-user-by-id/{id}")
    public StandardResponse<UserForFront> getById(
            @PathVariable UUID id
            ){
        return userService.getById(id);
    }





    @DeleteMapping("/{id}/delete-by-id")
    @PreAuthorize("hasRole('ADMIN') OR hasRole('OWNER')")
    public StandardResponse<String> delete(
            @PathVariable UUID id,
            Principal principal
    ){
        return userService.deleteUser(id, principal);
    }





    @PutMapping("/forgot-password")
    public StandardResponse<String> forgotPassword(
            @RequestParam String code,
            @RequestParam String newPassword,
            Principal principal
    ){
        return userService.forgotPassword(code, newPassword, principal);
    }





    @PutMapping("/{id}/assign-to-admin")
    @PreAuthorize("hasRole('OWNER')")
    public StandardResponse<UserForFront> assignToAdmin(
            @PathVariable UUID id,
            Principal principal
    ){
        return userService.assignToAdmin(id, principal);
    }





    @PutMapping("/update-profile/{id}")
    public StandardResponse<UserForFront> updateProfile(
            @PathVariable UUID id,
            Principal principal,
            @RequestBody UserDto userDto
            ){
        return userService.updateProfile(id, userDto, principal);
    }





    @GetMapping("/get-all-users")
    @PreAuthorize("hasRole('ADMIN') OR hasRole('OWNER')")
    public Page<UserEntity> getUsers(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int size) {
        return userService.getAll(page, size);
    }
}
