package uz.com.onlineshop.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.com.onlineshop.exception.DataNotFoundException;
import uz.com.onlineshop.exception.NotAcceptableException;
import uz.com.onlineshop.model.dto.request.user.UserDto;
import uz.com.onlineshop.model.dto.response.UserForFront;
import uz.com.onlineshop.model.entity.user.Gender;
import uz.com.onlineshop.model.entity.user.UserEntity;
import uz.com.onlineshop.model.entity.user.UserRole;
import uz.com.onlineshop.repository.UserRepository;
import uz.com.onlineshop.response.JwtResponse;
import uz.com.onlineshop.response.StandardResponse;
import uz.com.onlineshop.response.Status;
import uz.com.onlineshop.service.auth.JwtService;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public StandardResponse<JwtResponse> signUp (UserDto userDto){
       checkUserEmailAndPhoneNumber(userDto.getEmail(), userDto.getPhoneNumber());
       UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);
       userEntity.setPassword(passwordEncoder.encode(userDto.getPassword()));
       try {
           userEntity.setGender(Gender.valueOf(userDto.getGender()));
       }catch (Exception e){
           throw new NotAcceptableException("Gender not found");
       }
       userEntity.setAddress(userDto.getAddress());
       userEntity.setRole(UserRole.USER);
       userEntity.setFullName(userDto.getFullName());
       userEntity.setUsername(userDto.getUsername());
       userEntity.setEmail(userDto.getEmail());
       userEntity.setPhoneNumber(userDto.getPhoneNumber());
       userRepository.save(userEntity);
       String accessToken = jwtService.generateAccessToken(userEntity);
       String refreshToken = jwtService.generateRefreshToken(userEntity);
       UserForFront userForFront = modelMapper.map(userEntity, UserForFront.class);
       JwtResponse jwtResponse = JwtResponse.builder()
               .userForFront(userForFront)
               .accessToken(accessToken)
               .refreshToken(refreshToken)
               .build();
       return StandardResponse.<JwtResponse>builder()
               .data(jwtResponse)
               .status(Status.SUCCESS)
               .message("Sign up successfully!")
               .build();

    }

    private void checkUserEmailAndPhoneNumber(String email, String phoneNumber) {
        UserEntity userEntity = userRepository.findUserEntityByEmail(email);
        if (userEntity!=null){
            throw new DataNotFoundException("User has already exist");
                    }
       if (userRepository.findUserEntityByPhoneNumber(phoneNumber)!=null){
           throw new DataNotFoundException("User has already exist");
       }
    }
}
