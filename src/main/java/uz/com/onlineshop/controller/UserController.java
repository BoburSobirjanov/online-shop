package uz.com.onlineshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.com.onlineshop.model.dto.response.UserForFront;
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
    @PreAuthorize("hasRole('ADMIN')")
    public StandardResponse<String> delete(
            @PathVariable UUID id,
            Principal principal
    ){
        return userService.deleteUser(id, principal);
    }
}
