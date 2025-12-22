package com.polezhaiev.avtodiva.dto.car;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CreateCarRequestDto {
    @NotBlank(message = "Car name can't be empty")
    private String name;
}
