package com.polezhaiev.avtodiva.dto.schedule.event;

import com.polezhaiev.avtodiva.model.event.SlotEventType;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;

public record ScheduleSlotEventDto(
    String eventId,
    SlotEventType slotEventType,
    Instant timestamp,
    
    Long slotId,
    LocalDate date,
    LocalTime startTime,
    LocalTime endTime,
    
    Long instructorId,
    Long carId,
    Long studentId,
    
    Boolean booked
) {}