package com.polezhaiev.avtodiva.dto.student;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UpdateStudentRequestDto {
    @NotBlank(message = "Student name can't be empty")
    private String name;
}
