package uz.com.onlineshop.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.com.onlineshop.exception.DataNotFoundException;
import uz.com.onlineshop.exception.NotAcceptableException;
import uz.com.onlineshop.exception.UserBadRequestException;
import uz.com.onlineshop.model.dto.request.user.LoginDto;
import uz.com.onlineshop.model.dto.request.user.UserDto;
import uz.com.onlineshop.model.dto.response.UserForFront;
import uz.com.onlineshop.model.entity.user.Gender;
import uz.com.onlineshop.model.entity.user.UserEntity;
import uz.com.onlineshop.model.entity.user.UserRole;
import uz.com.onlineshop.model.entity.user.VerificationEntity;
import uz.com.onlineshop.repository.UserRepository;
import uz.com.onlineshop.repository.VerificationRepository;
import uz.com.onlineshop.response.JwtResponse;
import uz.com.onlineshop.response.StandardResponse;
import uz.com.onlineshop.response.Status;
import uz.com.onlineshop.service.auth.JwtService;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final VerificationRepository verificationRepository;





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









    public StandardResponse<JwtResponse> signIn(LoginDto loginDto){
          UserEntity userEntity = userRepository.findUserEntityByEmail(loginDto.getEmail());
          if (userEntity==null){
              throw new DataNotFoundException("User not found!");
          }
        if (passwordEncoder.matches(loginDto.getPassword(), userEntity.getPassword())){
           String accessToken = jwtService.generateAccessToken(userEntity);
           String refreshToken = jwtService.generateRefreshToken(userEntity);
           UserForFront userForFront =  modelMapper.map(userEntity, UserForFront.class);
           JwtResponse jwtResponse = JwtResponse.builder()
                   .refreshToken(refreshToken)
                   .accessToken(accessToken)
                   .userForFront(userForFront)
                   .build();
           return StandardResponse.<JwtResponse>builder()
                   .data(jwtResponse)
                   .status(Status.SUCCESS)
                   .message("Sign in successfully!")
                   .build();
        }
        else {
            throw new UserBadRequestException("Something error during sign in!");
        }
    }







    public StandardResponse<UserForFront> getById(UUID id){
        UserEntity userEntity = userRepository.findUserEntityById(id);
        if (userEntity==null){
            throw new DataNotFoundException("User not found!");
        }
        UserForFront userForFront = modelMapper.map(userEntity, UserForFront.class);
        return StandardResponse.<UserForFront>builder()
                .data(userForFront)
                .status(Status.SUCCESS)
                .message("This is user")
                .build();
    }






    public StandardResponse<String> deleteUser(UUID id, Principal principal){
        UserEntity user = userRepository.findUserEntityByEmail(principal.getName());
        UserEntity userEntity = userRepository.findUserEntityById(id);
        if (userEntity==null){
            throw new DataNotFoundException("User not found!");
        }
        userEntity.setDeleted(true);
        userEntity.setDeletedTime(LocalDateTime.now());
        userEntity.setDeletedBy(user.getId());
        userRepository.save(userEntity);

        return StandardResponse.<String>builder()
                .data("DELETED")
                .status(Status.SUCCESS)
                .message("User deleted successfully!")
                .build();
    }






    public StandardResponse<UserForFront> assignToAdmin(UUID id, Principal principal){
        UserEntity userEntity = userRepository.findUserEntityById(id);
        if (userEntity==null){
            throw new DataNotFoundException("User not found!");
        }
        userEntity.setRole(UserRole.ADMIN);
        userEntity.setChangeRoleBy(userRepository.findUserEntityByEmail(principal.getName()).getId());
        UserEntity save = userRepository.save(userEntity);
        UserForFront userForFront = modelMapper.map(save, UserForFront.class);

        return StandardResponse.<UserForFront>builder()
                .data(userForFront)
                .status(Status.SUCCESS)
                .message("User's role changed")
                .build();
    }







    public StandardResponse<String> forgotPassword(String code,String newPassword, Principal principal){
        UserEntity userEntity = userRepository.findUserEntityByEmail(principal.getName());
        VerificationEntity verification =  verificationRepository.findByUserEmailAndCode(userEntity.getId(),code);
        if (!verification.getCode().equals(code)){
            throw new NotAcceptableException("Verification code is incorrect!");
        }
        UserEntity user = userRepository.findUserEntityByEmail(principal.getName());
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        return StandardResponse.<String>builder()
                .data("CHANGED")
                .status(Status.SUCCESS)
                .message("Your password changed!")
                .build();
    }






    public StandardResponse<UserForFront> updateProfile(UUID id, UserDto userDto,Principal principal){
        UserEntity userEntity = userRepository.findUserEntityById(id);
        if (userEntity==null){
            throw new DataNotFoundException("User not found!");
        }
        userEntity.setEmail(userDto.getEmail());
        userEntity.setAddress(userDto.getAddress());
        userEntity.setUsername(userDto.getUsername());
        userEntity.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userEntity.setUpdatedBy(userRepository.findUserEntityByEmail(principal.getName()).getId());
        userEntity.setFullName(userDto.getFullName());
        userEntity.setPhoneNumber(userDto.getPhoneNumber());
        try {
            userEntity.setGender(Gender.valueOf(userDto.getGender()));
        } catch (Exception e){
            throw new NotAcceptableException("Invalid gender");
        }
        userEntity.setUpdatedTime(LocalDateTime.now());
        UserEntity save = userRepository.save(userEntity);
        UserForFront userForFront = modelMapper.map(save, UserForFront.class);
        return StandardResponse.<UserForFront>builder()
                .data(userForFront)
                .status(Status.SUCCESS)
                .message("User updated!")
                .build();
    }





    public Page<UserForFront> getAll(Pageable pageable){
        Page<UserEntity> users = userRepository.findAllUsers(pageable);
        return users.map(user -> new UserForFront(user.getId(), user.getFullName(), user.getPhoneNumber(), user.getUsername(),
                user.getEmail(), user.getAddress(), user.getRole(),user.getGender()));
    }
}
