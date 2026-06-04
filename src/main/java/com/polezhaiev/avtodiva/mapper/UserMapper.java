package com.polezhaiev.avtodiva.mapper;

import com.polezhaiev.avtodiva.config.MapperConfig;
import com.polezhaiev.avtodiva.dto.user.UserRegistrationRequestDto;
import com.polezhaiev.avtodiva.dto.user.UserResponseDto;
import com.polezhaiev.avtodiva.model.auth.Role;
import com.polezhaiev.avtodiva.model.auth.RoleResponseDto;
import com.polezhaiev.avtodiva.model.auth.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    @Mapping(target = "password", ignore = true)
    User toModel(UserRegistrationRequestDto requestDto);

    UserResponseDto toResponseDto(User user);

    default RoleResponseDto toRoleResponseDto(Role role) {
        if (role == null) {
            return null;
        }
        return new RoleResponseDto(role.getId(), role.getName());
    }
}
