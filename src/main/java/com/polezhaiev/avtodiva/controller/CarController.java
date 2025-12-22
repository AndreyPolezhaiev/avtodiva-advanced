package com.polezhaiev.avtodiva.controller;

import com.polezhaiev.avtodiva.dto.car.CarDto;
import com.polezhaiev.avtodiva.dto.car.CarResponseDto;
import com.polezhaiev.avtodiva.dto.car.CreateCarRequestDto;
import com.polezhaiev.avtodiva.mapper.CarMapper;
import com.polezhaiev.avtodiva.service.car.CarService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/cars")
public class CarController {
    private final CarService carService;
    private final CarMapper carMapper;

    @PostMapping
    public ResponseEntity<CarResponseDto> createCar(@RequestBody @Valid CreateCarRequestDto requestDto) {
        CarDto carDto = carMapper.toDto(requestDto);
        CarResponseDto carResponseDto = carService.saveCar(carDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(carResponseDto);
    }

    @GetMapping("/names")
    public ResponseEntity<List<CarResponseDto>> getAllCars() {
        List<CarResponseDto> allCarsNames = carService.getAllCarsNames();

        return ResponseEntity.status(HttpStatus.OK).body(allCarsNames);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarResponseDto> getCarById(@PathVariable Long id) {
        CarResponseDto carByName = carService.findCarById(id);

        return ResponseEntity.status(HttpStatus.OK).body(carByName);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        carService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
