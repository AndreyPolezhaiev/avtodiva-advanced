package com.polezhaiev.avtodiva.dto.template.time;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class ScheduleTemplateRequestDto {
    @NotNull
    private List<TimeSlotDto> intervals;
}