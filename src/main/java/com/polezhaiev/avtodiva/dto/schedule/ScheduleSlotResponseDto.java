package com.polezhaiev.avtodiva.dto.schedule;

import com.polezhaiev.avtodiva.dto.car.CarResponseDto;
import com.polezhaiev.avtodiva.dto.instructor.InstructorResponseDto;
import com.polezhaiev.avtodiva.dto.student.StudentResponseDto;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Accessors(chain = true)
public class ScheduleSlotResponseDto {
    private Long id;
    private LocalDate date;
    private LocalTime timeFrom;
    private LocalTime timeTo;
    private InstructorResponseDto instructorDto;
    private CarResponseDto carDto;
    private StudentResponseDto studentDto;
    private String description;
    private String link;
}
