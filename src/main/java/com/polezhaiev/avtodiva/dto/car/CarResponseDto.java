package com.polezhaiev.avtodiva.dto.car;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class CarResponseDto {
    private Long id;
    private String name;
    private BigDecimal lessonPrice;
}
