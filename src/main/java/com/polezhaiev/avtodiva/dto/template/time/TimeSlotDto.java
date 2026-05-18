package com.polezhaiev.avtodiva.dto.template.time;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalTime;

@Data
public class TimeSlotDto {
    @NotBlank
    private LocalTime startTime;
    @NotBlank
    private LocalTime endTime;
}