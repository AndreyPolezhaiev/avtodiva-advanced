package com.polezhaiev.avtodiva.dto.user;

import com.polezhaiev.avtodiva.validation.FieldMatch;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@FieldMatch(first = "password", second = "repeatPassword", message = "Passwords don't match")
public class UserRegistrationRequestDto {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 9, max = 32)
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{9,32}$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, one number and one special character"
    )
    private String password;

    @NotBlank
    private String repeatPassword;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;
}