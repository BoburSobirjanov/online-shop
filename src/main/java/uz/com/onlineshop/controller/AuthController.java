package uz.com.onlineshop.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import uz.com.onlineshop.standard.JwtResponse;
import uz.com.onlineshop.standard.StandardResponse;
import uz.com.onlineshop.service.MailSendingService;
import uz.com.onlineshop.service.UserService;

import java.security.Principal;
import java.util.List;


@RestController
@RequiredArgsConstructor
@Tag(name = "Authentication Controller", description = "Auth controller APIs for register and login")
@RequestMapping("/api/v1/auth")
@CrossOrigin
public class AuthController {

    private final UserService userService;
    private final MailSendingService mailSendingService;


    @Operation(summary = "Register a new user", description = "Registers a new user and returns a JWT token")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/sign-up")
    public ResponseEntity<StandardResponse<JwtResponse>> signUp(
            @Valid
            @RequestBody UserDto userDto,
            HttpServletRequest request,
            BindingResult bindingResult
    ) throws RequestValidationException {
        if (bindingResult.hasErrors()) {
            List<ObjectError> allErrors = bindingResult.getAllErrors();
            throw new RequestValidationException(allErrors);
        }
        IpAddressUtil.getClientIp(request);
        return ResponseEntity.ok(userService.signUp(userDto));
    }


    @Operation(summary = "User login", description = "Authenticates a user and returns a JWT token")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PostMapping("/sign-in")
    public StandardResponse<JwtResponse> signIn(
            @RequestBody LoginDto loginDto
    ) {
        return userService.signIn(loginDto);
    }


    @Operation(summary = "Send verification email", description = "Sends a verification email to the user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Verification email sent"),
            @ApiResponse(responseCode = "400", description = "Invalid email"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/send-verification")
    public StandardResponse<String> sendMessage(
            @RequestParam String email
    ) {
        return mailSendingService.sendMessage(email);
    }


    @PutMapping("/forgot-password")
    public StandardResponse<String> forgotPassword(
            @RequestParam String code,
            @RequestParam String newPassword,
            Principal principal
    ) {
        return userService.forgotPassword(code, newPassword, principal);
    }
}
