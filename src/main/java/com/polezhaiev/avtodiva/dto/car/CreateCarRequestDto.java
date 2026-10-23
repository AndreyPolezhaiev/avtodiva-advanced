package com.polezhaiev.avtodiva.dto.car;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class CreateCarRequestDto {
    @NotBlank(message = "Car name can't be empty")
    private String name;

    @NotNull(message = "Lesson price can't be empty")
    @PositiveOrZero(message = "Lesson price can't be negative")
    @Digits(integer = 8, fraction = 2, message = "Lesson price must have up to 8 integer and 2 fraction digits")
    private BigDecimal lessonPrice;
}
