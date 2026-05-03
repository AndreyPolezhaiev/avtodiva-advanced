package com.polezhaiev.avtodiva.dto.weekend;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Accessors(chain = true)
public class UpdateWeekendRequestDto {
    @NotNull(message = "Date can't be null")
    @FutureOrPresent(message = "Date must be today or in the future")
    private LocalDate date;

    @NotNull(message = "Time from can't be null")
    private LocalTime startTime;

    @NotNull(message = "Time to can't be null")
    private LocalTime endTime;

    @AssertTrue(message = "Time to must be after time from")
    public boolean isTimeRangeValid() {
        if (startTime == null || endTime == null) {
            return true;
        }
        return endTime.isAfter(startTime);
    }
}
