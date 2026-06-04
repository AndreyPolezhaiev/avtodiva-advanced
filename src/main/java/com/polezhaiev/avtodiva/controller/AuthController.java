package com.polezhaiev.avtodiva.controller;

import com.polezhaiev.avtodiva.dto.user.UpdateUserRolesRequestDto;
import com.polezhaiev.avtodiva.dto.user.UserLoginRequestDto;
import com.polezhaiev.avtodiva.dto.user.UserLoginResponseDto;
import com.polezhaiev.avtodiva.dto.user.UserRegistrationRequestDto;
import com.polezhaiev.avtodiva.security.AuthenticationService;
import com.polezhaiev.avtodiva.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public UserLoginResponseDto login(@RequestBody @Valid UserLoginRequestDto requestDto) {
        return authenticationService.authenticate(requestDto);
    }

    @PostMapping("/registration")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerUser(@RequestBody @Valid UserRegistrationRequestDto requestDto) {
        userService.register(requestDto);
    }
}