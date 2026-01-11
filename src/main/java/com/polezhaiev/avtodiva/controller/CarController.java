package com.polezhaiev.avtodiva.controller;

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

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cars")
public class CarController {
    private final CarService carService;

    @PostMapping
    public ResponseEntity<CarResponseDto> createCar(@RequestBody @Valid CreateCarRequestDto requestDto) {
        CarResponseDto carResponseDto = carService.save(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(carResponseDto);
    }

    @GetMapping("/all")
    public ResponseEntity<List<CarResponseDto>> getAllCars() {
        List<CarResponseDto> allCars = carService.findAll();

        return ResponseEntity.status(HttpStatus.OK).body(allCars);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarResponseDto> getCarById(@PathVariable Long id) {
        CarResponseDto carById = carService.findById(id);

        return ResponseEntity.status(HttpStatus.OK).body(carById);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCarById(@PathVariable Long id) {
        carService.deleteById(id);
        String response = "Car by id " + id + " was successfully removed";
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CarResponseDto> updateCarById(@PathVariable Long id,
                                                        @RequestBody @Valid CreateCarRequestDto requestDto) {
        CarResponseDto carResponseDto = carService.updateById(id, requestDto);

        return ResponseEntity.status(HttpStatus.OK).body(carResponseDto);
    }
}
