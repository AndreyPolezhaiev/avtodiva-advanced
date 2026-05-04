package com.polezhaiev.avtodiva.dto.weekend;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class WeekendSearchParametersDto {
    private LocalDate startDate;
    private LocalDate endDate;
    @NotEmpty
    private List<Long> instructorIds;
}
