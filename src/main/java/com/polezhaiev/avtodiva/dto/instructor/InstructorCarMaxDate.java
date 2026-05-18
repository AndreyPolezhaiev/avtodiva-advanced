package com.polezhaiev.avtodiva.dto.instructor;

import java.time.LocalDate;

public record InstructorCarMaxDate(Long instructorId, Long carId, LocalDate maxDate) {}