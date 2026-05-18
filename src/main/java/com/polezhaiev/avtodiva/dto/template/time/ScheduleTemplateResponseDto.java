package com.polezhaiev.avtodiva.dto.template.time;

import lombok.Data;

import java.util.List;

@Data
public class ScheduleTemplateResponseDto {
    private Long id;
    private List<TimeSlotDto> intervals;
}
