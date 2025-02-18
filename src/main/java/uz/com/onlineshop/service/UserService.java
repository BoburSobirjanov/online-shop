package uz.com.onlineshop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.com.onlineshop.exception.DataNotFoundException;
import uz.com.onlineshop.exception.NotAcceptableException;
import uz.com.onlineshop.exception.UserBadRequestException;
import uz.com.onlineshop.mapper.UserMapper;
import uz.com.onlineshop.model.dto.request.user.LoginDto;
import uz.com.onlineshop.model.dto.request.user.UserDto;
import uz.com.onlineshop.model.dto.response.UserForFrontDto;
import uz.com.onlineshop.model.enums.Gender;
import uz.com.onlineshop.model.entity.user.UserEntity;
import uz.com.onlineshop.model.enums.UserRole;
import uz.com.onlineshop.model.enums.UserStatus;
import uz.com.onlineshop.model.entity.verification.VerificationEntity;
import uz.com.onlineshop.repository.UserRepository;
import uz.com.onlineshop.repository.VerificationRepository;
import uz.com.onlineshop.standard.JwtResponse;
import uz.com.onlineshop.standard.StandardResponse;
import uz.com.onlineshop.service.auth.JwtService;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final VerificationRepository verificationRepository;
    private final UserMapper userMapper;


    public StandardResponse<JwtResponse> signUp(UserDto userDto) {
        checkUserEmailAndPhoneNumber(userDto.getEmail(), userDto.getPhoneNumber());
        UserEntity userEntity = userMapper.toEntity(userDto);
        userEntity.setPassword(passwordEncoder.encode(userDto.getPassword()));
        try {
            userEntity.setGender(Gender.valueOf(userDto.getGender()));
        } catch (Exception e) {
            throw new NotAcceptableException("Gender not found");
        }
        userEntity.setAddress(userDto.getAddress());
        userEntity.getRole().add(UserRole.USER);
        userEntity.setFullName(userDto.getFullName());
        userEntity.setUsername(userDto.getUsername());
        userEntity.setEmail(userDto.getEmail());
        userEntity.setUserStatus(UserStatus.ACTIVE);
        userEntity.setPhoneNumber(userDto.getPhoneNumber());
        userRepository.save(userEntity);
        String accessToken = jwtService.generateAccessToken(userEntity);
        String refreshToken = jwtService.generateRefreshToken(userEntity);
        UserForFrontDto userForFrontDto = userMapper.toDto(userEntity);
        JwtResponse jwtResponse = JwtResponse.builder()
                .userForFrontDto(userForFrontDto)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        return StandardResponse.ok("Signed up!", jwtResponse);
    }


    private void checkUserEmailAndPhoneNumber(String email, String phoneNumber) {
        UserEntity userEntity = userRepository.findUserEntityByEmail(email);
        if (userEntity != null) {
            throw new DataNotFoundException("User has already exist");
        }
        if (userRepository.findUserEntityByPhoneNumber(phoneNumber) != null) {
            throw new DataNotFoundException("User has already exist");
        }
    }


    public StandardResponse<JwtResponse> signIn(LoginDto loginDto) {
        UserEntity userEntity = userRepository.findUserEntityByEmail(loginDto.getEmail());
        if (userEntity == null || userEntity.getUserStatus() == UserStatus.BLOCKED) {
            throw new DataNotFoundException("User not found!");
        }
        if (passwordEncoder.matches(loginDto.getPassword(), userEntity.getPassword())) {
            String accessToken = jwtService.generateAccessToken(userEntity);
            String refreshToken = jwtService.generateRefreshToken(userEntity);
            UserForFrontDto userForFrontDto = userMapper.toDto(userEntity);
            JwtResponse jwtResponse = JwtResponse.builder()
                    .refreshToken(refreshToken)
                    .accessToken(accessToken)
                    .userForFrontDto(userForFrontDto)
                    .build();
            return StandardResponse.ok("Signed in", jwtResponse);
        } else {
            throw new UserBadRequestException("Something error during sign in!");
        }
    }


    public StandardResponse<UserForFrontDto> getById(UUID id) {
        UserEntity userEntity = userRepository.findUserEntityById(id);
        if (userEntity == null) {
            throw new DataNotFoundException("User not found!");
        }
        UserForFrontDto userForFrontDto = userMapper.toDto(userEntity);
        return StandardResponse.ok("This is user", userForFrontDto);
    }


    public StandardResponse<String> deleteUser(UUID id, Principal principal) {
        UserEntity user = userRepository.findUserEntityByEmail(principal.getName());
        UserEntity userEntity = userRepository.findUserEntityById(id);
        if (userEntity == null) {
            throw new DataNotFoundException("User not found!");
        }
        userEntity.setDeleted(true);
        userEntity.setDeletedTime(LocalDateTime.now());
        userEntity.setDeletedBy(user.getId());
        userRepository.save(userEntity);

        return StandardResponse.ok("User deleted!", "DELETED");
    }


    public StandardResponse<UserForFrontDto> assignToAdmin(UUID id, Principal principal) {
        UserEntity userEntity = userRepository.findUserEntityById(id);
        if (userEntity == null) {
            throw new DataNotFoundException("User not found!");
        }
        userEntity.getRole().add(UserRole.ADMIN);
        userEntity.setChangeRoleBy(userRepository.findUserEntityByEmail(principal.getName()).getId());
        UserEntity save = userRepository.save(userEntity);
        UserForFrontDto userForFrontDto = userMapper.toDto(save);

        return StandardResponse.ok("User's role changed!", userForFrontDto);
    }


    public StandardResponse<String> forgotPassword(String code, String newPassword, Principal principal) {
        UserEntity userEntity = userRepository.findUserEntityByEmail(principal.getName());
        VerificationEntity verification = verificationRepository.findByUserEmailAndCode(userEntity.getId(), code);
        if (!verification.getCode().equals(code) ||
                verification.getCreatedTime().plusMinutes(5).isBefore(LocalDateTime.now())) {
            throw new NotAcceptableException("Verification code is incorrect or expired!");
        }
        UserEntity user = userRepository.findUserEntityByEmail(principal.getName());
        user.setPassword(passwordEncoder.encode(newPassword));
        verificationRepository.delete(verification);
        userRepository.save(user);

        return StandardResponse.ok("User password changed!", "CHANGED");
    }


    public StandardResponse<UserForFrontDto> updateProfile(UUID id, UserDto userDto, Principal principal) {
        UserEntity userEntity = userRepository.findUserEntityById(id);
        if (userEntity == null) {
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
        } catch (Exception e) {
            throw new NotAcceptableException("Invalid gender");
        }
        userEntity.setUpdatedTime(LocalDateTime.now());
        UserEntity save = userRepository.save(userEntity);
        UserForFrontDto userForFrontDto = userMapper.toDto(save);
        return StandardResponse.ok("User updated!", userForFrontDto);
    }


    public Page<UserForFrontDto> getAll(Pageable pageable) {
        Page<UserEntity> users = userRepository.findAllUsers(pageable);
        return users.map(userMapper::toDto);
    }


    public StandardResponse<String> userBlocked(UUID id, Principal principal) {
        UserEntity userEntity = userRepository.findUserEntityById(id);
        if (userEntity == null || userEntity.getUserStatus() == UserStatus.BLOCKED) {
            throw new DataNotFoundException("User not found or this user has already blocked");
        }
        userEntity.setUserStatus(UserStatus.BLOCKED);
        userEntity.setBlockedBy(userRepository.findUserEntityByEmail(principal.getName()).getId());
        userRepository.save(userEntity);

        return StandardResponse.ok("User blocked!", "BLOCKED");
    }


    public StandardResponse<String> userActivated(UUID id, Principal principal) {
        UserEntity userEntity = userRepository.findUserEntityById(id);
        if (userEntity == null || userEntity.getUserStatus() == UserStatus.ACTIVE) {
            throw new DataNotFoundException("User not found or this user has already activated!");
        }
        userEntity.setUserStatus(UserStatus.ACTIVE);
        userEntity.setActiveBy(userRepository.findUserEntityByEmail(principal.getName()).getId());
        userRepository.save(userEntity);

        return StandardResponse.ok("User activated!", "ACTIVATED");
    }


    public Page<UserForFrontDto> getUserByStatus(Pageable pageable, String status) {
        Page<UserEntity> userEntities = userRepository.findUserEntityByUserStatus(pageable, status);
        return userEntities.map(userMapper::toDto);
    }


    public List<UserEntity> getAllUsersToExcel() {
        List<UserEntity> userEntities = userRepository.getAllUsersToExcel();
        if (userEntities == null) {
            throw new DataNotFoundException("Users not found!");
        }
        return userEntities;
    }


    public StandardResponse<String> multiDeleteById(List<String> id, Principal principal) {
        List<UserEntity> userList = userRepository.findAllById(id
                .stream()
                .map(UUID::fromString)
                .collect(Collectors.toList()));

        if (userList.isEmpty()) {
            throw new DataNotFoundException("Users not found!");
        }

        for (UserEntity user : userList) {
            user.setDeletedTime(LocalDateTime.now());
            user.setDeleted(true);
            user.setDeletedBy(userRepository.findUserEntityByEmail(principal.getName()).getId());
            userRepository.save(user);
        }

        return StandardResponse.ok("User deleted!", "DELETED");
    }


    public StandardResponse<UserForFrontDto> searchUserByNumber(String number) {
        UserEntity user = userRepository.findUserEntityByPhoneNumber(number);
        if (user == null) {
            throw new DataNotFoundException("User not found!");
        }
        UserForFrontDto userForFrontDto = userMapper.toDto(user);

        return StandardResponse.ok("This is user", userForFrontDto);
    }


    public Page<UserForFrontDto> searchByName(String name, Pageable pageable) {
        Page<UserEntity> userEntities = userRepository.findUserEntityByFullName(name, pageable);
        if (userEntities.isEmpty()) {
            throw new DataNotFoundException("User not found!");
        }

        return userEntities.map(userMapper::toDto);
    }
}
