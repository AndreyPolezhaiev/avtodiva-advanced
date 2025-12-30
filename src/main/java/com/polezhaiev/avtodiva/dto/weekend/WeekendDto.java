package com.polezhaiev.avtodiva.dto.weekend;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Accessors(chain = true)
public class WeekendDto {
    private Long id;
    private LocalDate day;
    private LocalTime timeFrom;
    private LocalTime timeTo;
    private Long instructorId;
}
