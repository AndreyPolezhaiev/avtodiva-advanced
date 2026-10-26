package com.polezhaiev.avtodiva.mapper;

import com.polezhaiev.avtodiva.config.MapperConfig;
import com.polezhaiev.avtodiva.dto.schedule.event.ScheduleSlotDomainEvent;
import com.polezhaiev.avtodiva.dto.schedule.event.ScheduleSlotEventDto;
import com.polezhaiev.avtodiva.model.ScheduleSlot;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Mapper(config = MapperConfig.class, imports = {UUID.class, Instant.class})
public interface ScheduleSlotEventMapper {
    BigDecimal MINUTES_IN_HOUR = BigDecimal.valueOf(60);

    @Mapping(target = "eventId", expression = "java(UUID.randomUUID().toString())")
    @Mapping(target = "slotEventType", source = "slotEventType")
    @Mapping(target = "timestamp", expression = "java(Instant.now())")
    @Mapping(target = "slotId", source = "slot.id")
    @Mapping(target = "date", source = "slot.date")
    @Mapping(target = "startTime", source = "slot.startTime")
    @Mapping(target = "endTime", source = "slot.endTime")
    @Mapping(target = "instructorName", source = "slot.instructor.name")
    @Mapping(target = "carName", source = "slot.car.name")
    @Mapping(target = "studentName", source = "slot.student.name")
    @Mapping(target = "lessonPrice", source = "slot", qualifiedByName = "calculateLessonPrice")
    @Mapping(target = "booked", source = "slot.booked")
    ScheduleSlotEventDto toDto(ScheduleSlotDomainEvent event);

    /**
     * Car price is set per hour, so the lesson price depends on the slot duration
     */
    @Named("calculateLessonPrice")
    default BigDecimal calculateLessonPrice(ScheduleSlot slot) {
        if (slot.getCar() == null || slot.getCar().getLessonPrice() == null
                || slot.getStartTime() == null || slot.getEndTime() == null) {
            return null;
        }

        long minutes = Duration.between(slot.getStartTime(), slot.getEndTime()).toMinutes();
        if (minutes <= 0) {
            return BigDecimal.ZERO;
        }

        return slot.getCar().getLessonPrice()
                .multiply(BigDecimal.valueOf(minutes))
                .divide(MINUTES_IN_HOUR, 2, RoundingMode.HALF_UP);
    }
}
