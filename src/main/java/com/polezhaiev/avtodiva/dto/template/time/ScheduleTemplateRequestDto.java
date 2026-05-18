package com.polezhaiev.avtodiva.dto.template.time;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class ScheduleTemplateRequestDto {
    @NotBlank
    private List<TimeSlotDto> intervals;
}