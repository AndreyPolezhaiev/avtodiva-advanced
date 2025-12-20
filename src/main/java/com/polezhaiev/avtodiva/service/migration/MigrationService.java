package com.polezhaiev.avtodiva.service.migration;

import com.polezhaiev.avtodiva.model.ScheduleSlot;
import com.polezhaiev.avtodiva.repository.ScheduleSlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MigrationService {
    private final ScheduleSlotRepository scheduleSlotRepository;

    @Transactional
    public void migrateSlotsToNewRules() {
        List<ScheduleSlot> allSlots = scheduleSlotRepository.findAll();

        for (ScheduleSlot slot : allSlots) {
            String instructor = slot.getInstructor().getName();
            LocalDate date = slot.getDate();
            LocalTime from = slot.getTimeFrom();

            // 1. Delete all 18:30 entries for everyone except Dina and Yulia
            if (from.equals(LocalTime.of(18, 30))
                    && !(instructor.equalsIgnoreCase("Діна") || instructor.equalsIgnoreCase("Юлія"))) {
                scheduleSlotRepository.delete(slot);
                continue;
            }

            // 2. For Dina
            if (instructor.equalsIgnoreCase("Діна")) {
                // Saturday 6:30 PM → delete
                if (date.getDayOfWeek() == DayOfWeek.SATURDAY && from.equals(LocalTime.of(18, 30))) {
                    scheduleSlotRepository.delete(slot);
                    continue;
                }

                // Saturday 3:00 PM → reschedule for 4:00 PM
                if (date.getDayOfWeek() == DayOfWeek.SATURDAY && from.equals(LocalTime.of(15, 0))) {
                    slot.setTimeFrom(LocalTime.of(16, 0));
                    slot.setTimeTo(LocalTime.of(19, 0));
                    scheduleSlotRepository.save(slot);
                    continue;
                }

                // any 18:30 → change to 18:15
                if (from.equals(LocalTime.of(18, 30))) {
                    slot.setTimeFrom(LocalTime.of(18, 15));
                    slot.setTimeTo(LocalTime.of(20, 15));
                    scheduleSlotRepository.save(slot);
                    continue;
                }
            }

            // 3. For Yulia
            if (instructor.equalsIgnoreCase("Юлія")) {
                if (from.equals(LocalTime.of(18, 30))) {
                    slot.setTimeFrom(LocalTime.of(18, 15));
                    slot.setTimeTo(LocalTime.of(20, 15));
                    scheduleSlotRepository.save(slot);
                }
            }
        }
    }

    @Transactional
    public void makeAllForOneHourEarlier() {
        List<ScheduleSlot> allSlots = scheduleSlotRepository.findAll();

        for (ScheduleSlot slot : allSlots) {
            LocalDate date = slot.getDate();

            if (date.getMonth().getValue() >= 12) {
                slot.setTimeFrom(slot.getTimeFrom().minusHours(1));
                slot.setTimeTo(slot.getTimeTo().minusHours(1));
            }
        }
    }

    @Transactional
    public void removeFreeSlotsForTanya() {
        LocalDate dateFrom = LocalDate.of(2025, 12, 18);
        List<ScheduleSlot> freeTanyaFordSlots = scheduleSlotRepository.findFreeSlotsFromDate("Таня", "Ford", dateFrom);
        scheduleSlotRepository.deleteAll(freeTanyaFordSlots);
    }

    @Transactional
    public void removeAllFreeSlots() {
        List<ScheduleSlot> allFreeSlots = scheduleSlotRepository.findAllFreeSlots();
        scheduleSlotRepository.deleteAll(allFreeSlots);
    }

    @Transactional
    public void removeAllFreeSlotsByInstructorName() {
        List<ScheduleSlot> allFreeDinaSlots = scheduleSlotRepository.findAllFreeSlotsByInstructorName("Діна");
        List<ScheduleSlot> allFreeYuliaSlots = scheduleSlotRepository.findAllFreeSlotsByInstructorName("Юлія");
        scheduleSlotRepository.deleteAll(allFreeDinaSlots);
        scheduleSlotRepository.deleteAll(allFreeYuliaSlots);
    }
}
