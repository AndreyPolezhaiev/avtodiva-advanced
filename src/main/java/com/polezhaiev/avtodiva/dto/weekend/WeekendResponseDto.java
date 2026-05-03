package com.polezhaiev.avtodiva.dto.weekend;

import com.polezhaiev.avtodiva.dto.instructor.InstructorResponseDto;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Accessors(chain = true)
public class WeekendResponseDto {
    private Long id;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private InstructorResponseDto instructorDto;
}
