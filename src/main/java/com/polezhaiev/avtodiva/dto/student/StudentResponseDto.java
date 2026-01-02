package com.polezhaiev.avtodiva.dto.student;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class StudentResponseDto {
    private Long id;
    private String name;
}
