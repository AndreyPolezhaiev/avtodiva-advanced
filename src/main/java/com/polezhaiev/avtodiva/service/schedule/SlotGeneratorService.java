package com.polezhaiev.avtodiva.service.schedule;

import com.polezhaiev.avtodiva.model.Car;
import com.polezhaiev.avtodiva.model.Instructor;
import com.polezhaiev.avtodiva.model.ScheduleSlot;
import com.polezhaiev.avtodiva.repository.CarRepository;
import com.polezhaiev.avtodiva.repository.InstructorRepository;
import com.polezhaiev.avtodiva.repository.ScheduleSlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static com.polezhaiev.avtodiva.service.schedule.util.WorkingHoursProvider.getWorkingHours;

@Service
@RequiredArgsConstructor
public class SlotGeneratorService {
    private final ScheduleSlotRepository scheduleSlotRepository;
    private final CarRepository carRepository;
    private final InstructorRepository instructorRepository;

    public void addFreeWindowsForEachInstructor(int days) {
        List<Instructor> allInstructors = instructorRepository.findAll();
        List<Car> allCars = carRepository.findAll();

        for (Instructor instructor : allInstructors) {
            for (Car car : allCars) {
                LocalDate lastDate = scheduleSlotRepository.findMaxFreeDateByInstructorAndCar(instructor, car);
                LocalDate startDate = lastDate != null ? lastDate.plusDays(1) : LocalDate.now();

                for (int i = 0; i < days; i++) {
                    LocalDate targetDate = startDate.plusDays(i);
                    int[][] hours = getWorkingHours(instructor.getName(), targetDate);

                    for (int j = 0; j < hours.length; j++) {
                        LocalTime from = LocalTime.of(hours[j][0], hours[j][1]);
                        LocalTime to = (j == hours.length - 1 && hours.length > 1 && hours[j][0] >= 17) ? from.plusHours(2) : from.plusHours(3);

                        boolean exists = scheduleSlotRepository.existsByDateAndTimeFromAndInstructorAndCar(
                                targetDate, from, instructor, car
                        );
                        if (exists) continue;

                        ScheduleSlot slot = new ScheduleSlot();
                        slot.setDate(targetDate);
                        slot.setTimeFrom(from);
                        slot.setTimeTo(to);
                        slot.setInstructor(instructor);
                        slot.setCar(car);
                        slot.setStudent(null);
                        slot.setDescription(null);
                        slot.setLink(null);
                        slot.setBooked(false);

                        scheduleSlotRepository.save(slot);
                    }
                }
            }
        }
    }

    public void addFreeWindowsForCar(String carName, int days) {
        Car car = carRepository.findByName(carName)
                .orElseThrow(() -> new RuntimeException("Car by name: " + carName + " not found"));

        List<Instructor> allInstructors = instructorRepository.findAll();

        for (Instructor instructor : allInstructors) {
            LocalDate lastDate = scheduleSlotRepository.findMaxFreeDateByInstructorAndCar(instructor, car);
            LocalDate startDate = lastDate != null ? lastDate.plusDays(1) : LocalDate.now();

            for (int i = 0; i < days; i++) {
                LocalDate targetDate = startDate.plusDays(i);
                int[][] hours = getWorkingHours(instructor.getName(), targetDate);

                for (int j = 0; j < hours.length; j++) {
                    LocalTime from = LocalTime.of(hours[j][0], hours[j][1]);
                    LocalTime to = (j == hours.length - 1 && hours.length > 1 && hours[j][0] >= 17)
                            ? from.plusHours(2)
                            : from.plusHours(3);

                    boolean exists = scheduleSlotRepository.existsByDateAndTimeFromAndInstructorAndCar(
                            targetDate, from, instructor, car
                    );
                    if (exists) continue;

                    ScheduleSlot slot = new ScheduleSlot();
                    slot.setDate(targetDate);
                    slot.setTimeFrom(from);
                    slot.setTimeTo(to);
                    slot.setInstructor(instructor);
                    slot.setCar(car);
                    slot.setBooked(false);

                    scheduleSlotRepository.save(slot);
                }
            }
        }
    }

    public void addFreeWindowsForInstructor(String instructorName, int days) {
        Instructor instructor = instructorRepository.findByName(instructorName)
                .orElseThrow(() -> new RuntimeException("Instructor by name: " + instructorName + " not found"));

        List<Car> allCars = carRepository.findAll();

        for (Car car : allCars) {
            LocalDate lastDate = scheduleSlotRepository.findMaxFreeDateByInstructorAndCar(instructor, car);
            LocalDate startDate = lastDate != null ? lastDate.plusDays(1) : LocalDate.now();

            for (int i = 0; i < days; i++) {
                LocalDate targetDate = startDate.plusDays(i);
                int[][] hours = getWorkingHours(instructor.getName(), targetDate);

                for (int j = 0; j < hours.length; j++) {
                    LocalTime from = LocalTime.of(hours[j][0], hours[j][1]);
                    LocalTime to = (j == hours.length - 1 && hours.length > 1 && hours[j][0] >= 17)
                            ? from.plusHours(2)
                            : from.plusHours(3);

                    boolean exists = scheduleSlotRepository.existsByDateAndTimeFromAndInstructorAndCar(
                            targetDate, from, instructor, car
                    );
                    if (exists) continue;

                    ScheduleSlot slot = new ScheduleSlot();
                    slot.setDate(targetDate);
                    slot.setTimeFrom(from);
                    slot.setTimeTo(to);
                    slot.setInstructor(instructor);
                    slot.setCar(car);
                    slot.setBooked(false);

                    scheduleSlotRepository.save(slot);
                }
            }
        }
    }
}
