package com.polezhaiev.avtodiva.mapper;

import com.polezhaiev.avtodiva.config.MapperConfig;
import com.polezhaiev.avtodiva.dto.schedule.event.ScheduleSlotDomainEvent;
import com.polezhaiev.avtodiva.dto.schedule.event.ScheduleSlotEventDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;
import java.util.UUID;

@Mapper(config = MapperConfig.class, imports = {UUID.class, Instant.class})
public interface ScheduleSlotEventMapper {

    @Mapping(target = "eventId", expression = "java(UUID.randomUUID().toString())")
    @Mapping(target = "slotEventType", source = "slotEventType")
    @Mapping(target = "timestamp", expression = "java(Instant.now())")
    @Mapping(target = "slotId", source = "slot.id")
    @Mapping(target = "date", source = "slot.date")
    @Mapping(target = "startTime", source = "slot.startTime")
    @Mapping(target = "endTime", source = "slot.endTime")
    @Mapping(target = "instructorId", source = "slot.instructor.id")
    @Mapping(target = "carId", source = "slot.car.id")
    @Mapping(target = "studentId", source = "slot.student.id")
    @Mapping(target = "booked", source = "slot.booked")
    ScheduleSlotEventDto toDto(ScheduleSlotDomainEvent event);
}
