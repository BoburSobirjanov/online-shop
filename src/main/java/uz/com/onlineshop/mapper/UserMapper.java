package uz.com.onlineshop.mapper;

import org.mapstruct.Mapper;
import uz.com.onlineshop.model.dto.request.user.UserDto;
import uz.com.onlineshop.model.dto.response.UserForFrontDto;
import uz.com.onlineshop.model.entity.user.UserEntity;

@Mapper(componentModel = "spring")
public interface UserMapper{

    UserForFrontDto toDto(UserEntity user);

    UserEntity toEntity(UserDto request);
}
