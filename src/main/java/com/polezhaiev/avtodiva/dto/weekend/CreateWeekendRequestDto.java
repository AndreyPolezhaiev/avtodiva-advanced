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
public class CreateWeekendRequestDto {
    @NotNull(message = "Day can't be null")
    @FutureOrPresent(message = "Day must be today or in the future")
    private LocalDate day;

    @NotNull(message = "Time from can't be null")
    private LocalTime timeFrom;

    @NotNull(message = "Time to can't be null")
    private LocalTime timeTo;

    @NotNull(message = "Instructor id can't be null")
    private Long instructorId;

    @AssertTrue(message = "Time to must be after time from")
    public boolean isTimeRangeValid() {
        if (timeFrom == null || timeTo == null) {
            return true;
        }
        return timeTo.isAfter(timeFrom);
    }
}
