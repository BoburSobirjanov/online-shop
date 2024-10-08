package uz.com.onlineshop.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import uz.com.onlineshop.filter.IpAddressUtil;
import uz.com.onlineshop.model.dto.request.user.LoginDto;
import uz.com.onlineshop.model.dto.request.user.UserDto;
import uz.com.onlineshop.response.JwtResponse;
import uz.com.onlineshop.response.StandardResponse;
import uz.com.onlineshop.service.MailSendingService;
import uz.com.onlineshop.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserService userService;
    private final MailSendingService mailSendingService;




    @PostMapping("/sign-up")
    public StandardResponse<JwtResponse> signUp(
            @RequestBody UserDto userDto,
            HttpServletRequest request
            ){
        String clientIp= IpAddressUtil.getClientIp(request);
        return userService.signUp(userDto);
    }





    @PostMapping("/sign-in")
    public StandardResponse<JwtResponse> signIn(
            @RequestBody LoginDto loginDto
            ){
        return userService.signIn(loginDto);
    }






    @GetMapping("/send-verification")
    public StandardResponse<String> sendMessage(
          @RequestParam String email
    ){
      return  mailSendingService.sendMessage(email);
    }
}
