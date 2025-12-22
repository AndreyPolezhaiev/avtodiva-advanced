package com.polezhaiev.avtodiva.service.car;

import com.polezhaiev.avtodiva.dto.car.CarDto;
import com.polezhaiev.avtodiva.dto.car.CarResponseDto;
import com.polezhaiev.avtodiva.mapper.CarMapper;
import com.polezhaiev.avtodiva.model.Car;
import com.polezhaiev.avtodiva.repository.CarRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CarService {
    private final CarRepository carRepository;
    private final CarMapper carMapper;

    @Caching(evict = {
            @CacheEvict(value = "carNames", allEntries = true),
            @CacheEvict(value = "carByName", key = "#car.name")
    })
    public CarResponseDto saveCar(CarDto carDto) {
        if (carRepository.existsByNameIgnoreCase(carDto.getName())) {
            throw new IllegalStateException("Машина з іменем '" + carDto.getName() + "' вже існує!");
        }

        Car car = carMapper.toModel(carDto);
        car.setSlots(new ArrayList<>());

        Car savedCar = carRepository.save(car);

        return carMapper.toResponseDto(savedCar);
    }

    @Cacheable("carNames")
    public List<CarResponseDto> getAllCarsNames() {
        return carRepository.findAll()
                .stream()
                .map(carMapper::toResponseDto)
                .toList();
    }

    @Cacheable("carByName")
    public CarResponseDto findCarById(Long id) {
        Car carFromRepo = carRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Can't find car by id: " + id)
        );

        return carMapper.toResponseDto(carFromRepo);
    }

    @Transactional
    public void deleteById(Long id) {
        carRepository.deleteById(id);
    }
}
