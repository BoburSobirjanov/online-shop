package uz.com.onlineshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.com.onlineshop.model.dto.request.user.UserDto;
import uz.com.onlineshop.response.JwtResponse;
import uz.com.onlineshop.response.StandardResponse;
import uz.com.onlineshop.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserService userService;
    @PostMapping("/sign-up")
    public StandardResponse<JwtResponse> signUp(
            @RequestBody UserDto userDto
            ){
        return userService.signUp(userDto);
    }
}
