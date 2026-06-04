package com.polezhaiev.avtodiva.dto.user;

import com.polezhaiev.avtodiva.model.auth.RoleResponseDto;
import lombok.Data;

import java.util.List;

@Data
public class UserResponseDto {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private List<RoleResponseDto> roles;
}
