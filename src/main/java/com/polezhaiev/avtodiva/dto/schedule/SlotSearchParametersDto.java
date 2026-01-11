package com.polezhaiev.avtodiva.dto.schedule;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class SlotSearchParametersDto {
    private List<Long> instructorIds;
    private List<Long> carIds;
    private Long studentId;
    private LocalDate dateFrom;
    private LocalDate dateTo;
    private Boolean booked;
}