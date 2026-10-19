package com.polezhaiev.avtodiva.service.schedule.listener;

import com.polezhaiev.avtodiva.dto.schedule.event.ScheduleSlotDomainEvent;
import com.polezhaiev.avtodiva.dto.schedule.event.ScheduleSlotEventDto;
import com.polezhaiev.avtodiva.mapper.ScheduleSlotEventMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class SlotAnalyticsEventListener {
    private final KafkaTemplate<String, ScheduleSlotEventDto> kafkaTemplate;
    private final ScheduleSlotEventMapper slotEventMapper;

    @Value("${app.kafka.topics.analytics-slots}")
    private String topicName;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleSlotEvent(ScheduleSlotDomainEvent event) {
        ScheduleSlotEventDto eventDto = slotEventMapper.toDto(event);

        log.info("Sending slot analytics event to Kafka topic {}: {}", topicName, eventDto.eventId());

        kafkaTemplate.send(topicName, eventDto.slotId().toString(), eventDto);
    }
}
