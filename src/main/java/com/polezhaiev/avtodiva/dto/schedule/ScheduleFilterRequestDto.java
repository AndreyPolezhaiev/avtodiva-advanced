package com.polezhaiev.avtodiva.dto.schedule;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ScheduleFilterRequestDto {
    private List<Long> instructorIds;
    private List<Long> carIds;
    private Long studentId;
    private LocalDate from;
    private LocalDate to;
    private Boolean booked;
}