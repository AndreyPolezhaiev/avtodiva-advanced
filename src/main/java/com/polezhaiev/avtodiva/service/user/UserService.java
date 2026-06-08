package com.polezhaiev.avtodiva.service.user;

import com.polezhaiev.avtodiva.dto.user.UpdateUserRolesRequestDto;
import com.polezhaiev.avtodiva.dto.user.UserRegistrationRequestDto;
import com.polezhaiev.avtodiva.dto.user.UserResponseDto;
import com.polezhaiev.avtodiva.mapper.UserMapper;
import com.polezhaiev.avtodiva.model.auth.Role;
import com.polezhaiev.avtodiva.model.auth.RoleName;
import com.polezhaiev.avtodiva.model.auth.User;
import com.polezhaiev.avtodiva.repository.auth.RoleRepository;
import com.polezhaiev.avtodiva.repository.auth.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public void register(UserRegistrationRequestDto requestDto) {
        if (userRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new RuntimeException("User with this email already exists: " + requestDto.getEmail());
        }

        User user = userMapper.toModel(requestDto);

        String hashedPassword = passwordEncoder.encode(requestDto.getPassword());
        user.setPassword(hashedPassword);

        Role defaultRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Default role ROLE_USER not found in the database. " +
                        "Please check if database migrations were executed."));

        user.setRoles(Set.of(defaultRole));

        userRepository.save(user);
    }

    public void updateUserRoles(Long userId, UpdateUserRolesRequestDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Set<Role> newRoles = requestDto.roleNames().stream()
                .map(roleName -> roleRepository.findByName(roleName)
                        .orElseThrow(() -> new RuntimeException("Role not found: " + roleName)))
                .collect(Collectors.toSet());

        user.setRoles(newRoles);

        userRepository.save(user);
    }

    public List<UserResponseDto> findAll() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toResponseDto)
                .toList();
    }

    public void deleteById(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new RuntimeException("There is no registered user by id: " + id)
        );

        userRepository.delete(user);
    }
}