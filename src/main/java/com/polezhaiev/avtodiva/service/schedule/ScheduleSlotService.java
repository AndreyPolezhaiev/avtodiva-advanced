package com.polezhaiev.avtodiva.service.schedule;

import com.polezhaiev.avtodiva.dto.schedule.CreateScheduleSlotRequestDto;
import com.polezhaiev.avtodiva.dto.schedule.SlotSearchParametersDto;
import com.polezhaiev.avtodiva.dto.schedule.ScheduleSlotResponseDto;
import com.polezhaiev.avtodiva.dto.schedule.UpdateScheduleSlotRequestDto;
import com.polezhaiev.avtodiva.mapper.ScheduleSlotMapper;
import com.polezhaiev.avtodiva.model.Car;
import com.polezhaiev.avtodiva.model.Instructor;
import com.polezhaiev.avtodiva.model.ScheduleSlot;
import com.polezhaiev.avtodiva.model.Student;
import com.polezhaiev.avtodiva.repository.*;
import com.polezhaiev.avtodiva.repository.spec.impl.ScheduleSlotSpecificationBuilder;
import com.polezhaiev.avtodiva.service.schedule.util.ScheduleValidatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ScheduleSlotService {
    private final ScheduleSlotRepository scheduleSlotRepository;
    private final InstructorRepository instructorRepository;
    private final CarRepository carRepository;
    private final StudentRepository studentRepository;
    private final ScheduleSlotMapper scheduleSlotMapper;
    private final ScheduleValidatorService scheduleValidatorService;
    private final ScheduleSlotSpecificationBuilder specificationBuilder;

    public ScheduleSlotResponseDto create(CreateScheduleSlotRequestDto requestDto) {
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
        newSlot.setBooked(true);
        ScheduleSlot saved = scheduleSlotRepository.save(newSlot);

        ScheduleSlotResponseDto responseDto = scheduleSlotMapper.toResponseDto(saved);
        responseDto.setBooked(saved.getBooked());
        return responseDto;
    }

    public List<ScheduleSlotResponseDto> searchSlots(SlotSearchParametersDto searchParameters) {
        Specification<ScheduleSlot> slotSpecification = specificationBuilder.build(searchParameters);
        Sort sort = Sort.by(
                Sort.Order.asc("date"),
                Sort.Order.asc("startTime"),
                Sort.Order.asc("instructor.name"),
                Sort.Order.asc("car.name")
        );

        return scheduleSlotRepository.findAll(slotSpecification, sort)
                .stream()
                .map(scheduleSlotMapper::toResponseDto)
                .toList();
    }

    public ScheduleSlotResponseDto findById(Long id) {
        ScheduleSlot scheduleSlotFromRepo = scheduleSlotRepository.findSlotById(id).orElseThrow(
                () -> new RuntimeException("Can't find slot by id: " + id)
        );

        return scheduleSlotMapper.toResponseDto(scheduleSlotFromRepo);
    }

    public ScheduleSlotResponseDto findLastBookedByStudentId(Long studentId) {
        return scheduleSlotRepository.findLastBookedStudentSlot(studentId)
                .map(scheduleSlotMapper::toResponseDto)
                .orElse(null);
    }

    /**
     * Makes the slot free
     *
     * @param id
     */
    public void deleteById(Long id) {
        ScheduleSlot scheduleSlotFromRepo = scheduleSlotRepository.findSlotById(id).orElseThrow(
                () -> new RuntimeException("Can't find slot by id: " + id)
        );

        scheduleSlotFromRepo.setStudent(null);
        scheduleSlotFromRepo.setDescription(null);
        scheduleSlotFromRepo.setLink(null);
        scheduleSlotFromRepo.setBooked(false);

        scheduleSlotRepository.save(scheduleSlotFromRepo);
    }

    public void deleteSlotsByCarId(Long carId) {
        scheduleSlotRepository.softDeleteAllByCarId(carId);
    }

    public void deleteSlotsByInstructorId(Long instructorId) {
        scheduleSlotRepository.softDeleteAllByInstructorId(instructorId);
    }

    public void clearSlotsByStudentId(Long studentId) {
        scheduleSlotRepository.releaseAllSlotsByStudentId(studentId);
    }

    public ScheduleSlotResponseDto updateById(Long id, UpdateScheduleSlotRequestDto requestDto) {
        ScheduleSlot existing = scheduleSlotRepository.findSlotById(id).orElseThrow(
                () -> new IllegalArgumentException("Can't find slot by id: " + id)
        );

        if (scheduleValidatorService.areKeyFieldsChanged(existing, requestDto)) {
            scheduleValidatorService.checkScheduleConflictsExcluding(requestDto, existing.getId());
        }

        Student student = null;
        if (requestDto.getStudentId() != null) {
            student = studentRepository.findById(requestDto.getStudentId()).orElseThrow(
                    () -> new RuntimeException("Can't find student by id: " + requestDto.getStudentId())
            );
        }

        Instructor instructor = instructorRepository.findById(requestDto.getInstructorId()).orElseThrow(
                () -> new RuntimeException("Can't find instructor by id: " + requestDto.getInstructorId())
        );
        Car car = carRepository.findById(requestDto.getCarId()).orElseThrow(
                () -> new RuntimeException("Can't find car by id: " + requestDto.getCarId())
        );

        existing.setDescription(requestDto.getDescription());
        existing.setLink(requestDto.getLink());
        existing.setStudent(student);
        existing.setInstructor(instructor);
        existing.setCar(car);
        existing.setStartTime(requestDto.getStartTime());
        existing.setEndTime(requestDto.getEndTime());
        existing.setDate(requestDto.getDate());
        existing.setBooked(student != null);

        ScheduleSlot saved = scheduleSlotRepository.save(existing);

        return scheduleSlotMapper.toResponseDto(saved);
    }
}
