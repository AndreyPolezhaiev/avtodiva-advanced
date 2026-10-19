package com.polezhaiev.avtodiva.dto.schedule.event;

import com.polezhaiev.avtodiva.model.ScheduleSlot;
import com.polezhaiev.avtodiva.model.event.SlotEventType;

public record ScheduleSlotDomainEvent(
    ScheduleSlot slot,
    SlotEventType slotEventType
) {}