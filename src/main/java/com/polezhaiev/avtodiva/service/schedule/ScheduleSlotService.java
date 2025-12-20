package com.polezhaiev.avtodiva.service.schedule;

import com.polezhaiev.avtodiva.model.Car;
import com.polezhaiev.avtodiva.model.Instructor;
import com.polezhaiev.avtodiva.model.ScheduleSlot;
import com.polezhaiev.avtodiva.repository.CarRepository;
import com.polezhaiev.avtodiva.repository.InstructorRepository;
import com.polezhaiev.avtodiva.repository.ScheduleSlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class ScheduleSlotService {
    private final ScheduleSlotRepository scheduleSlotRepository;
    private final InstructorRepository instructorRepository;
    private final CarRepository carRepository;

    public List<ScheduleSlot> findAllSlots(List<String> instructorNames, List<String> carNames, LocalDate start, LocalDate end) {
        List<String> instructors = instructorNames == null ? List.of() :
                instructorNames.stream().map(String::toLowerCase).toList();
        List<String> cars = carNames == null ? List.of() :
                carNames.stream().map(String::toLowerCase).toList();

        List<ScheduleSlot> allSlotsFromRepo = scheduleSlotRepository.findAllSlots(instructors, cars, start, end);

        return allSlotsFromRepo;
    }

    public List<ScheduleSlot> findBookedSlots(List<String> instructorNames, List<String> carNames, LocalDate start, LocalDate end) {
        List<String> instructors = instructorNames == null ? List.of() :
                instructorNames.stream().map(String::toLowerCase).toList();
        List<String> cars = carNames == null ? List.of() :
                carNames.stream().map(String::toLowerCase).toList();

        return scheduleSlotRepository.findBookedSlotsBetween(instructors, cars, start, end);
    }

    public List<ScheduleSlot> findFreeSlots(List<String> instructorNames, List<String> carNames, LocalDate start, LocalDate end) {
        List<String> instructors = instructorNames == null ? List.of() :
                instructorNames.stream().map(String::toLowerCase).toList();
        List<String> cars = carNames == null ? List.of() :
                carNames.stream().map(String::toLowerCase).toList();

        List<ScheduleSlot> freeSlotsFromRepo = scheduleSlotRepository.findFreeSlotsBetween(instructors, cars, start, end);

        return freeSlotsFromRepo;
    }

    @CacheEvict(value = "studentNames", allEntries = true)
    public void updateSlot(ScheduleSlot slot) {
        scheduleSlotRepository.save(slot);
    }

    private void createExceptionSlot(ScheduleSlot exceptionSlot) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEEE, dd.MM.yyyy", new Locale("uk", "UA"));

        if (exceptionSlot.getTimeFrom().isAfter(LocalTime.of(14,59))
                && !exceptionSlot.getInstructor().getName().equalsIgnoreCase("Марина")) {
            throw new IllegalStateException("Помилка: У інструктора "
                    + exceptionSlot.getInstructor().getName()
                    + " не має місць на "
                    + exceptionSlot.getDate().format(dateFormatter)
                    + " о "
                    + exceptionSlot.getTimeFrom().toString());
        }

        Instructor instructor = instructorRepository.findByName(exceptionSlot.getInstructor().getName()).orElse(null);
        Car car = carRepository.findByName(exceptionSlot.getCar().getName()).orElse(null);

        if (instructor != null && scheduleSlotRepository.existsWeekendConflict(
                instructor,
                exceptionSlot.getDate(),
                exceptionSlot.getTimeFrom(),
                exceptionSlot.getTimeTo()
        )) {
            throw new IllegalStateException("Виключний слот інструктора потрапляє у вихідний/неробочий час!");
        }

        if (car != null && scheduleSlotRepository.existsBookedInstructorAndCarConflict(
                instructor,
                car,
                exceptionSlot.getDate(),
                exceptionSlot.getTimeFrom(),
                exceptionSlot.getTimeTo()
        )) {
            throw new IllegalStateException("Ця машина або інструктор вже зайнята у цей час!");
        }

        exceptionSlot.setInstructor(instructor);
        exceptionSlot.setCar(car);
        scheduleSlotRepository.save(exceptionSlot);
    }

    @Transactional
    @CacheEvict(value = "studentNames", allEntries = true)
    public void createSlot(ScheduleSlot newSlot) {
        ScheduleSlot existing = scheduleSlotRepository.findByInstructorCarDateTime(
                newSlot.getInstructor().getName().toLowerCase(),
                newSlot.getCar().getName().toLowerCase(),
                newSlot.getDate(),
                newSlot.getTimeFrom()
        ).orElse(null);

        if (existing != null) {
            if (existing.getStudent() != null) {
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEEE, dd.MM.yyyy", new Locale("uk", "UA"));

                throw new IllegalStateException("Помилка: Запис на "
                        + existing.getDate().format(dateFormatter)
                        + " о "
                        + existing.getTimeFrom().toString()
                        + " вже існує!");
            }
            existing.setStudent(newSlot.getStudent());
            existing.setDescription(newSlot.getDescription());
            existing.setLink(newSlot.getLink());
            existing.setBooked(newSlot.isBooked());
            rescheduleSlot(existing);

        } else if (newSlot.getInstructor().getName().equalsIgnoreCase("Юлія")) {
            createExceptionSlot(newSlot);

        } else if (newSlot.getInstructor().getName().equalsIgnoreCase("Марина")) {
            createExceptionSlot(newSlot);

        } else if (newSlot.getInstructor().getName().equalsIgnoreCase("Діна")) {
            createExceptionSlot(newSlot);
        } else {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEEE, dd.MM.yyyy", new Locale("uk", "UA"));

            throw new IllegalStateException("Помилка: У інструктора "
                    + newSlot.getInstructor().getName()
                    + " не має місць на "
                    + newSlot.getDate().format(dateFormatter)
                    + " о "
                    + newSlot.getTimeFrom().toString());
        }
    }

    @Transactional
    @CacheEvict(value = "studentNames", allEntries = true)
    public boolean rescheduleSlot(ScheduleSlot slot) {
        if (slot.getStudent() == null || slot.getStudent().getName() == null || slot.getStudent().getName().isBlank()) {
            return false;
        }
        // We retrieve the current slot from the database by ID.
        ScheduleSlot existing = scheduleSlotRepository.findById(slot.getId())
                .orElseThrow(() -> new IllegalArgumentException("Slot not found"));
        Car carFromRepo = carRepository.findByName(slot.getCar().getName())
                .orElseThrow(() -> new IllegalArgumentException("Slot not found"));

        // We check if the key fields have changed
        boolean changed =
                !existing.getInstructor().getName().equalsIgnoreCase(slot.getInstructor().getName()) ||
                        !existing.getCar().getName().equalsIgnoreCase(slot.getCar().getName()) ||
                        !existing.getDate().equals(slot.getDate()) ||
                        !existing.getTimeFrom().equals(slot.getTimeFrom());

        Instructor instructor = instructorRepository.findByName(slot.getInstructor().getName()).orElse(null);
        if (instructor != null && scheduleSlotRepository.existsWeekendConflict(
                instructor,
                slot.getDate(),
                slot.getTimeFrom(),
                slot.getTimeTo()
        )) {
            throw new IllegalStateException("Новий слот інструктора потрапляє у вихідний/неробочий час!");
        }

        if (!changed) {
            if (scheduleSlotRepository.existsBookedInstructorAndCarConflictExcluding(
                    instructor,
                    carFromRepo,
                    slot.getDate(),
                    slot.getTimeFrom(),
                    slot.getTimeTo(),
                    existing.getId()
            )) {
                throw new IllegalStateException("Цей інструктор або машина вже зайняті у цей час!");
            }

            // Nothing important has changed → just updating
            existing.setDescription(slot.getDescription());
            existing.setLink(slot.getLink());
            existing.setStudent(slot.getStudent());
            existing.setBooked(slot.isBooked());
            scheduleSlotRepository.save(existing);
            return true;
        }

        // 1. free up the old slot
        existing.setBooked(false);
        existing.setStudent(null);
        existing.setDescription("Якщо запис є у вільних місцях то можна зайняти, інакше буде помилка");
        existing.setLink("Якщо запис є у вільних місцях то можна зайняти, інакше буде помилка");
        scheduleSlotRepository.save(existing);

        // 2. looking for a slot with new parameters
        ScheduleSlot target = scheduleSlotRepository.findByInstructorCarDateTime(
                slot.getInstructor().getName().toLowerCase(),
                slot.getCar().getName().toLowerCase(),
                slot.getDate(),
                slot.getTimeFrom()
        ).orElse(null);

        if (target != null) {
            if (target.isBooked()) {
                throw new IllegalStateException("Target slot already booked!");
            }

            // Check: The machine should not have any other occupied slots at this time.
            if (scheduleSlotRepository.existsBookedCarConflictExcluding(
                    carFromRepo,
                    slot.getDate(),
                    slot.getTimeFrom(),
                    slot.getTimeTo(),
                    target.getId() // exclude the slot itself, otherwise it will always cause a conflict
            )) {
                throw new IllegalStateException("Ця машина вже зайнята у цей час!");
            }

            if (scheduleSlotRepository.existsBookedInstructorConflictExcluding(
                    instructor,
                    slot.getDate(),
                    slot.getTimeFrom(),
                    slot.getTimeTo(),
                    existing.getId()
            )) {
                throw new IllegalStateException("Цей інструктор вже зайнят у цей час!");
            }

            // we occupy the found slot
            target.setBooked(true);
            target.setStudent(slot.getStudent());
            target.setDescription(slot.getDescription());
            target.setLink(slot.getLink());
            scheduleSlotRepository.save(target);
            return true;
        } else {

            if (scheduleSlotRepository.existsBookedInstructorAndCarConflict(
                    instructor,
                    carFromRepo,
                    slot.getDate(),
                    slot.getTimeFrom(),
                    slot.getTimeTo()
            )) {
                throw new IllegalStateException("Цей інструктор або машина вже зайняті у цей час!");
            }

            // If such a slot doesn't exist yet, create a new one.
            ScheduleSlot created = new ScheduleSlot();
            created.setDate(slot.getDate());
            created.setTimeFrom(slot.getTimeFrom());
            created.setTimeTo(slot.getTimeTo());
            created.setInstructor(slot.getInstructor());
            created.setCar(slot.getCar());
            created.setStudent(slot.getStudent());
            created.setDescription(slot.getDescription());
            created.setLink(slot.getLink());
            created.setBooked(true);
            scheduleSlotRepository.save(created);
            return true;
        }
    }

    public List<ScheduleSlot> findByInstructorAndStudentNames(String instructorName, String studentName) {
        return scheduleSlotRepository.findByInstructorNameIgnoreCaseAndStudentNameIgnoreCase(instructorName, studentName);
    }

    public List<ScheduleSlot> findByInstructorName(String instructorName) {
        return scheduleSlotRepository.findByInstructorNameIgnoreCase(instructorName);
    }

    public List<ScheduleSlot> findByStudentName(String studentName) {
        return scheduleSlotRepository.findByStudentNameIgnoreCase(studentName.trim());
    }

    public List<ScheduleSlot> filterSlotsByTime(List<ScheduleSlot> slots, List<String> selectedTimes) {
        return slots.stream()
                .filter(slot -> selectedTimes.contains(slot.getTimeFrom().toString()))
                .toList();
    }

    public List<ScheduleSlot> findAllBookedSlotsByInstructorName(String instructorName) {
        return scheduleSlotRepository.findAllBookedSlotsByInstructorName(instructorName);
    }
}
