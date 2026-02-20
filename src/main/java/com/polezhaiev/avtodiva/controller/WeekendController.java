package com.polezhaiev.avtodiva.controller;

import com.polezhaiev.avtodiva.dto.weekend.CreateWeekendRequestDto;
import com.polezhaiev.avtodiva.dto.weekend.UpdateWeekendRequestDto;
import com.polezhaiev.avtodiva.dto.weekend.WeekendResponseDto;
import com.polezhaiev.avtodiva.mapper.WeekendMapper;
import com.polezhaiev.avtodiva.service.weekend.WeekendService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/weekends")
public class WeekendController {
    private final WeekendService weekendService;
    private final WeekendMapper weekendMapper;

    @PostMapping
    public ResponseEntity<WeekendResponseDto> createWeekend(@RequestBody @Valid CreateWeekendRequestDto requestDto) {
        WeekendResponseDto weekendResponseDto = weekendService.save(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(weekendResponseDto);
    }

    @GetMapping("/all")
    public ResponseEntity<List<WeekendResponseDto>> getAllWeekends() {
        List<WeekendResponseDto> allWeekends = weekendService.findAll();

        return ResponseEntity.status(HttpStatus.OK).body(allWeekends);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WeekendResponseDto> getCarById(@PathVariable Long id) {
        WeekendResponseDto weekendById = weekendService.findById(id);

        return ResponseEntity.status(HttpStatus.OK).body(weekendById);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WeekendResponseDto> updateWeekendById(@PathVariable Long id,
                                                                @RequestBody @Valid UpdateWeekendRequestDto requestDto) {
        WeekendResponseDto weekendResponseDto = weekendService.updateById(id, requestDto);

        return ResponseEntity.status(HttpStatus.OK).body(weekendResponseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteWeekendById(@PathVariable Long id) {
        weekendService.deleteById(id);
        String response = "Weekend by id " + id + " was successfully removed";
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
