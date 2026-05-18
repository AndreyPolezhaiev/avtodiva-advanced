package com.polezhaiev.avtodiva.dto.schedule.generation;

import java.time.LocalDate;

public record InstructorCarMaxDateDto(Long instructorId, Long carId, LocalDate maxDate) {}