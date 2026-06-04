package com.polezhaiev.avtodiva.dto.user;

import com.polezhaiev.avtodiva.model.auth.RoleName;
import java.util.Set;

public record UpdateUserRolesRequestDto(Set<RoleName> roleNames) {
}