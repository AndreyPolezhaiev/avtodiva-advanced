package com.polezhaiev.avtodiva.dto.instructor;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CreateInstructorRequestDto {
    @NotBlank(message = "Instructor name can't be empty")
    private String name;
}
