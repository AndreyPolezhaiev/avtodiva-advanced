package com.polezhaiev.avtodiva.service.schedule;

import com.polezhaiev.avtodiva.dto.schedule.generation.InstructorCarMaxDateDto;
import com.polezhaiev.avtodiva.dto.schedule.generation.SlotGenerationRequestDto;
import com.polezhaiev.avtodiva.model.Car;
import com.polezhaiev.avtodiva.model.Instructor;
import com.polezhaiev.avtodiva.model.ScheduleSlot;
import com.polezhaiev.avtodiva.model.template.time.TimeSlot;
import com.polezhaiev.avtodiva.repository.CarRepository;
import com.polezhaiev.avtodiva.repository.InstructorRepository;
import com.polezhaiev.avtodiva.repository.ScheduleSlotRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class SlotGeneratorService {
    private static final String JOIN = "-";

    private final ScheduleSlotRepository scheduleSlotRepository;
    private final CarRepository carRepository;
    private final InstructorRepository instructorRepository;

    public void addFreeWindowsForEachInstructor(SlotGenerationRequestDto requestDto) {
        int days = requestDto.getDays();

        List<Instructor> instructors =
                (requestDto.getInstructorIds() == null || requestDto.getInstructorIds().isEmpty())
                    ? instructorRepository.findAll()
                    : instructorRepository.findAllById(requestDto.getInstructorIds());

        List<Car> cars = (requestDto.getCarIds() == null || requestDto.getCarIds().isEmpty())
                ? carRepository.findAll()
                : carRepository.findAllById(requestDto.getCarIds());

        if (instructors.isEmpty() || cars.isEmpty()) {
            throw new IllegalStateException("Can't generate slots: Cars or Instructors don't exists");
        }

        List<ScheduleSlot> newSlots = new ArrayList<>();

        List<Long> instructorIds = instructors.stream().map(Instructor::getId).toList();
        List<Long> carIds = cars.stream().map(Car::getId).toList();

        Map<String, LocalDate> maxDateMap = scheduleSlotRepository.findAllMaxDatesGrouped(instructorIds, carIds)
                .stream()
                .collect(Collectors.toMap(
                        dto -> generateKey(dto.instructorId(), dto.carId()),
                        InstructorCarMaxDateDto::maxDate
                ));

        LocalDate rangeStart = maxDateMap.values().stream()
                .min(LocalDate::compareTo)
                .orElse(LocalDate.now());

        LocalDate rangeEnd = maxDateMap.values().stream()
                .max(LocalDate::compareTo)
                .orElse(LocalDate.now())
                .plusDays(days);

        Set<String> existingKeys = scheduleSlotRepository.findFilteredByDateBetween(rangeStart, rangeEnd, instructorIds, carIds)
                .stream()
                .map(this::generateKey)
                .collect(Collectors.toSet());

        for (Instructor instructor : instructors) {
            List<TimeSlot> timeIntervals = instructor.getScheduleTemplate().getIntervals();
            if (timeIntervals == null || timeIntervals.isEmpty()) continue;

            for (Car car : cars) {
                String mapKey = generateKey(instructor.getId(), car.getId());
                LocalDate endDate = maxDateMap.get(mapKey);
                LocalDate startDate = (endDate != null) ? endDate.plusDays(1) : LocalDate.now();

                for (int i = 0; i < days; i++) {
                    LocalDate targetDate = startDate.plusDays(i);

                    timeIntervals.forEach(timeSlot -> {
                        LocalTime startTime = timeSlot.getStartTime();
                        String currentKey = generateKey(targetDate, startTime, instructor, car);

                        if (!existingKeys.contains(currentKey)) {
                            ScheduleSlot slot = createNewSlot(targetDate, timeSlot, instructor, car);
                            newSlots.add(slot);
                        }
                    });
                }
            }
        }

        if (!newSlots.isEmpty()) {
            scheduleSlotRepository.saveAll(newSlots);
        }
    }

    private String generateKey(LocalDate date, LocalTime time, Instructor instructor, Car car) {
        return date.toString() + "_" + time.toString() + "_" + instructor.getId() + "_" + car.getId();
    }

    private String generateKey(ScheduleSlot slot) {
        return generateKey(slot.getDate(), slot.getStartTime(), slot.getInstructor(), slot.getCar());
    }

    private String generateKey(long instructorId, long carId) {
        return instructorId + JOIN + carId;
    }

    private ScheduleSlot createNewSlot(LocalDate date, TimeSlot timeSlot, Instructor instructor, Car car) {
        ScheduleSlot slot = new ScheduleSlot();
        slot.setDate(date);
        slot.setStartTime(timeSlot.getStartTime());
        slot.setEndTime(timeSlot.getEndTime());
        slot.setInstructor(instructor);
        slot.setCar(car);
        slot.setStudent(null);
        slot.setDescription(null);
        slot.setLink(null);
        slot.setBooked(false);

        return slot;
    }
}
