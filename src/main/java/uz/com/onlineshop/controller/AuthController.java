package uz.com.onlineshop.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import uz.com.onlineshop.exception.RequestValidationException;
import uz.com.onlineshop.filter.IpAddressUtil;
import uz.com.onlineshop.model.dto.request.user.LoginDto;
import uz.com.onlineshop.model.dto.request.user.UserDto;
import uz.com.onlineshop.response.JwtResponse;
import uz.com.onlineshop.response.StandardResponse;
import uz.com.onlineshop.service.MailSendingService;
import uz.com.onlineshop.service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserService userService;
    private final MailSendingService mailSendingService;




    @PostMapping("/sign-up")
    public ResponseEntity<StandardResponse<JwtResponse>> signUp(
            @Valid
            @RequestBody UserDto userDto,
            HttpServletRequest request,
            BindingResult bindingResult
            ) throws RequestValidationException{
        if (bindingResult.hasErrors()){
        List<ObjectError> allErrors = bindingResult.getAllErrors();
        throw new RequestValidationException(allErrors);
    }
        String clientIp= IpAddressUtil.getClientIp(request);
        return ResponseEntity.ok(userService.signUp(userDto));
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
