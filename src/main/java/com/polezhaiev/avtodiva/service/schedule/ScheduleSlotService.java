package com.polezhaiev.avtodiva.service.schedule;

import com.polezhaiev.avtodiva.dto.schedule.CreateScheduleSlotRequestDto;
import com.polezhaiev.avtodiva.dto.schedule.SlotSearchParametersDto;
import com.polezhaiev.avtodiva.dto.schedule.ScheduleSlotResponseDto;
import com.polezhaiev.avtodiva.mapper.ScheduleSlotMapper;
import com.polezhaiev.avtodiva.model.Car;
import com.polezhaiev.avtodiva.model.Instructor;
import com.polezhaiev.avtodiva.model.ScheduleSlot;
import com.polezhaiev.avtodiva.model.Student;
import com.polezhaiev.avtodiva.repository.*;
import com.polezhaiev.avtodiva.repository.spec.impl.ScheduleSlotSpecificationBuilder;
import com.polezhaiev.avtodiva.service.schedule.util.ScheduleValidatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
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
    private final ScheduleSlotSpecificationBuilder specificationBuilder;

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
        newSlot.setBooked(true);
        ScheduleSlot saved = scheduleSlotRepository.save(newSlot);

        return scheduleSlotMapper.toResponseDto(saved);
    }

    public List<ScheduleSlotResponseDto> searchSlots(SlotSearchParametersDto searchParameters) {
        Specification<ScheduleSlot> slotSpecification = specificationBuilder.build(searchParameters);
        return scheduleSlotRepository.findAll(slotSpecification)
                .stream()
                .map(scheduleSlotMapper::toResponseDto)
                .toList();
    }

    public ScheduleSlotResponseDto findById(Long id) {
        ScheduleSlot scheduleSlotFromRepo = scheduleSlotRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Can't find slot by id: " + id)
        );

        return scheduleSlotMapper.toResponseDto(scheduleSlotFromRepo);
    }

    /**
     * Makes the slot free
     *
     * @param id
     */
    public void deleteById(Long id) {
        ScheduleSlot scheduleSlotFromRepo = scheduleSlotRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Can't find slot by id: " + id)
        );

        scheduleSlotFromRepo.setStudent(null);
        scheduleSlotFromRepo.setDescription(null);
        scheduleSlotFromRepo.setLink(null);

        scheduleSlotRepository.save(scheduleSlotFromRepo);
    }

    @Transactional
    public ScheduleSlotResponseDto updateById(Long id, CreateScheduleSlotRequestDto requestDto) {
        ScheduleSlot existing = scheduleSlotRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Can't find slot by id: " + id)
        );
        Student student = studentRepository.findById(requestDto.getStudentId()).orElseThrow(
                () -> new RuntimeException("Can't find student by id: " + requestDto.getStudentId())
        );
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
        existing.setTimeFrom(requestDto.getTimeFrom());
        existing.setTimeTo(requestDto.getTimeTo());
        existing.setDate(requestDto.getDate());
        existing.setBooked(true);

        scheduleValidatorService.checkScheduleConflictsExcluding(requestDto, existing.getId());

        ScheduleSlot saved = scheduleSlotRepository.save(existing);

        return scheduleSlotMapper.toResponseDto(saved);
    }
}
