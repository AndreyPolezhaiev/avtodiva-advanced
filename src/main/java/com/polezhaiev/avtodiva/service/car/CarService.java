package com.polezhaiev.avtodiva.service.car;

import com.polezhaiev.avtodiva.dto.car.CarResponseDto;
import com.polezhaiev.avtodiva.dto.car.CreateCarRequestDto;
import com.polezhaiev.avtodiva.mapper.CarMapper;
import com.polezhaiev.avtodiva.model.Car;
import com.polezhaiev.avtodiva.repository.CarRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CarService {
    private final CarRepository carRepository;
    private final CarMapper carMapper;

    public CarResponseDto save(CreateCarRequestDto requestDto) {
        if (carRepository.existsByNameIgnoreCase(requestDto.getName())) {
            throw new IllegalStateException("Car with name '" + requestDto.getName() + "' already exists!");
        }

        Car car = carMapper.toModel(requestDto);
        car.setSlots(new ArrayList<>());

        Car savedCar = carRepository.save(car);

        return carMapper.toResponseDto(savedCar);
    }

    public List<CarResponseDto> findAll() {
        return carRepository.findAll()
                .stream()
                .map(carMapper::toResponseDto)
                .toList();
    }

    public CarResponseDto findById(Long id) {
        Car carFromRepo = carRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Can't find car by id: " + id)
        );

        return carMapper.toResponseDto(carFromRepo);
    }

    @Transactional
    public void deleteById(Long id) {
        Car carFromRepo = carRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Can't find car by id: " + id)
        );

        carRepository.delete(carFromRepo);
    }

    public CarResponseDto updateById(Long id, CreateCarRequestDto requestDto) {
        Car carFromRepo = carRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Can't find car by id: " + id)
        );

        carFromRepo.setName(requestDto.getName());

        Car updatedCar = carRepository.save(carFromRepo);

        return carMapper.toResponseDto(updatedCar);
    }
}
