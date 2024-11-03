package uz.com.onlineshop.mapper;

import org.mapstruct.Mapper;
import uz.com.onlineshop.model.dto.response.UserForFrontDto;
import uz.com.onlineshop.model.entity.user.UserEntity;

@Mapper(componentModel = "spring")
public interface UserMapper extends EntityMapper<UserEntity, UserForFrontDto>{

    UserForFrontDto toDto(UserEntity user);
}
