package com.polezhaiev.avtodiva.service.schedule.util;

import com.polezhaiev.avtodiva.dto.schedule.CreateScheduleSlotRequestDto;
import com.polezhaiev.avtodiva.model.ScheduleSlot;
import com.polezhaiev.avtodiva.repository.ScheduleSlotRepository;
import com.polezhaiev.avtodiva.repository.WeekendRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduleValidatorService {
    private final WeekendRepository weekendRepository;
    private final ScheduleSlotRepository scheduleSlotRepository;

    public void checkScheduleConflicts(CreateScheduleSlotRequestDto requestDto) {
        if (weekendRepository.existsWeekendConflict(
                requestDto.getInstructorId(),
                requestDto.getDate(),
                requestDto.getTimeFrom(),
                requestDto.getTimeTo()
        )) {
            throw new IllegalStateException("The slot falls during off/non-working hours!");
        }

        if (scheduleSlotRepository.existsBookedInstructorAndCarConflict(
                requestDto.getInstructorId(),
                requestDto.getCarId(),
                requestDto.getDate(),
                requestDto.getTimeFrom(),
                requestDto.getTimeTo()
        )) {
            throw new IllegalStateException("That car or instructor is already busy at this time!");
        }
    }

    public void checkScheduleConflictsExcluding(CreateScheduleSlotRequestDto requestDto, Long excludedSlotId) {
        if (weekendRepository.existsWeekendConflict(
                requestDto.getInstructorId(),
                requestDto.getDate(),
                requestDto.getTimeFrom(),
                requestDto.getTimeTo()
        )) {
            throw new IllegalStateException("The slot falls during off/non-working hours!");
        }

        if (scheduleSlotRepository.existsBookedInstructorAndCarConflictExcluding(
                requestDto.getInstructorId(),
                requestDto.getCarId(),
                requestDto.getDate(),
                requestDto.getTimeFrom(),
                requestDto.getTimeTo(),
                excludedSlotId // exclude the slot itself, otherwise it will always cause a conflict
        )) {
            throw new IllegalStateException("This instructor or car is already busy at this time!");
        }
    }

    public void checkSlotExists(CreateScheduleSlotRequestDto requestDto) {
        if (scheduleSlotRepository.existsByInstructorIdAndCarIdAndDateAndTimeFrom(
                requestDto.getInstructorId(),
                requestDto.getCarId(),
                requestDto.getDate(),
                requestDto.getTimeFrom()))
        {
            throw new IllegalStateException("Error: The slot by instructor id: "
                    + requestDto.getInstructorId()
                    + " and car id: "
                    + requestDto.getCarId()
                    + " and date: "
                    + requestDto.getDate().toString()
                    + " and time from "
                    + requestDto.getTimeFrom().toString()
                    + " already exists");
        }
    }

    /**
     * Checks if the key fields have changed
     *
     * @param existing
     * @param requestDto
     * @return
     */
    public boolean areKeyFieldsChanged(ScheduleSlot existing, CreateScheduleSlotRequestDto requestDto) {
        return !existing.getInstructor().getId().equals(requestDto.getInstructorId()) ||
                !existing.getCar().getId().equals(requestDto.getCarId()) ||
                !existing.getDate().equals(requestDto.getDate()) ||
                !existing.getTimeFrom().equals(requestDto.getTimeFrom());
    }
}
