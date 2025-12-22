package com.polezhaiev.avtodiva.dto.car;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CarDto {
    private Long id;
    private String name;
}
