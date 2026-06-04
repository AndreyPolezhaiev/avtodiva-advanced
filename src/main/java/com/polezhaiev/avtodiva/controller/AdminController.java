package com.polezhaiev.avtodiva.controller;

import com.polezhaiev.avtodiva.dto.user.UpdateUserRolesRequestDto;
import com.polezhaiev.avtodiva.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/users")
public class AdminController {
    private final UserService userService;

    @PutMapping("/{id}/roles")
    @PreAuthorize("hasRole('ADMIN')")
    public void updateUserRoles(
            @PathVariable("id") Long userId,
            @RequestBody UpdateUserRolesRequestDto requestDto
    ) {
        userService.updateUserRoles(userId, requestDto);
    }
}