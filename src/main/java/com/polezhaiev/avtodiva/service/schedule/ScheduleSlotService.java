package com.polezhaiev.avtodiva.service.schedule;

import com.polezhaiev.avtodiva.dto.schedule.CreateScheduleSlotRequestDto;
import com.polezhaiev.avtodiva.dto.schedule.ScheduleFilterRequestDto;
import com.polezhaiev.avtodiva.dto.schedule.ScheduleSlotResponseDto;
import com.polezhaiev.avtodiva.mapper.ScheduleSlotMapper;
import com.polezhaiev.avtodiva.model.Car;
import com.polezhaiev.avtodiva.model.Instructor;
import com.polezhaiev.avtodiva.model.ScheduleSlot;
import com.polezhaiev.avtodiva.model.Student;
import com.polezhaiev.avtodiva.repository.*;
import com.polezhaiev.avtodiva.service.schedule.util.ScheduleValidatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleSlotService {
    private final ScheduleSlotRepository scheduleSlotRepository;
    private final InstructorRepository instructorRepository;
    private final CarRepository carRepository;
    private final StudentRepository studentRepository;
    private final ScheduleSlotMapper scheduleSlotMapper;
    private final ScheduleValidatorService scheduleValidatorService;

    public ScheduleSlotResponseDto create(CreateScheduleSlotRequestDto requestDto) {
        scheduleValidatorService.checkSlotExists(requestDto);
        scheduleValidatorService.checkScheduleConflicts(requestDto);

        Instructor instructor = instructorRepository.findById(requestDto.getInstructorId()).orElseThrow(
                () -> new RuntimeException("Can't find instructor by id: " + requestDto.getInstructorId())
        );
        Car car = carRepository.findById(requestDto.getCarId()).orElseThrow(
                () -> new RuntimeException("Can't find car by id: " + requestDto.getCarId())
        );
        Student student = studentRepository.findById(requestDto.getStudentId()).orElseThrow(
                () -> new RuntimeException("Can't find student by id: " + requestDto.getStudentId())
        );

        ScheduleSlot newSlot = scheduleSlotMapper.toModel(requestDto);

        newSlot.setInstructor(instructor);
        newSlot.setCar(car);
        newSlot.setStudent(student);
        ScheduleSlot saved = scheduleSlotRepository.save(newSlot);

        return scheduleSlotMapper.toResponseDto(saved);
    }

    public List<ScheduleSlotResponseDto> searchSlots(ScheduleFilterRequestDto filter) {
        return scheduleSlotRepository.findWithFilter(
                filter.getInstructorIds(),
                filter.getCarIds(),
                filter.getStudentId(),
                filter.getFrom(),
                filter.getTo(),
                filter.getBooked()
        ).stream()
                .map(scheduleSlotMapper::toResponseDto)
                .toList();
    }

    public ScheduleSlotResponseDto findById(Long id) {
        ScheduleSlot scheduleSlotFromRepo = scheduleSlotRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Can't find slot by id: " + id)
        );

        return scheduleSlotMapper.toResponseDto(scheduleSlotFromRepo);
    }

    public void deleteById(Long id) {
        ScheduleSlot scheduleSlotFromRepo = scheduleSlotRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Can't find slot by id: " + id)
        );

        scheduleSlotRepository.delete(scheduleSlotFromRepo);
    }

    @Transactional
    public ScheduleSlotResponseDto updateById(Long id, CreateScheduleSlotRequestDto requestDto) {
        ScheduleSlot existing = scheduleSlotRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Can't find slot by id: " + id)
        );

        Student studentFromRepo = studentRepository.findById(requestDto.getStudentId()).orElseThrow(
                () -> new RuntimeException("Can't find student by id: " + requestDto.getStudentId())
        );

        if (!scheduleValidatorService.areKeyFieldsChanged(existing, requestDto)) {
            existing.setDescription(requestDto.getDescription());
            existing.setLink(requestDto.getLink());
            existing.setStudent(studentFromRepo);
            existing.setBooked(true);
            return scheduleSlotMapper.toResponseDto(scheduleSlotRepository.save(existing));
        }

        ScheduleSlot target = scheduleSlotRepository.findByInstructorCarDateTime(
                requestDto.getInstructorId(),
                requestDto.getCarId(),
                requestDto.getDate(),
                requestDto.getTimeFrom()
        ).orElseThrow(
                () -> new IllegalStateException("Target slot not found! Create the slot first.")
        );

        if (target.isBooked() && !target.getId().equals(existing.getId())) {
            throw new IllegalStateException("Target slot already booked!");
        }

        // Check: The instructor and car should not have any other occupied slots at this time.
        scheduleValidatorService.checkScheduleConflictsExcluding(requestDto, target.getId());

        // 1. free up the old slot
        existing.setBooked(false);
        existing.setStudent(null);
        existing.setDescription(null);
        existing.setLink(null);
        scheduleSlotRepository.save(existing);

        // we occupy the found slot
        target.setBooked(true);
        target.setStudent(studentFromRepo);
        target.setDescription(requestDto.getDescription());
        target.setLink(requestDto.getLink());

        ScheduleSlot saved = scheduleSlotRepository.save(target);

        return scheduleSlotMapper.toResponseDto(saved);
    }
}
