package com.polezhaiev.avtodiva.dto.instructor;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class InstructorResponseDto {
    private Long id;
    private String name;
}
