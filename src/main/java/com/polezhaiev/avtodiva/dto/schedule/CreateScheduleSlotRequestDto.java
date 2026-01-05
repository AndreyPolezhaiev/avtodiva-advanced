package com.polezhaiev.avtodiva.dto.schedule;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Accessors(chain = true)
public class CreateScheduleSlotRequestDto {
    @NotNull
    private LocalDate date;
    @NotNull
    private LocalTime timeFrom;
    @NotNull
    private LocalTime timeTo;
    @NotNull
    private Long instructorId;
    @NotNull
    private Long carId;
    @NotNull
    private Long studentId;
    private String description;
    private String link;
}
