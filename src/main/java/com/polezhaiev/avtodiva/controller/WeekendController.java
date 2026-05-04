package com.polezhaiev.avtodiva.controller;

import com.polezhaiev.avtodiva.dto.weekend.CreateWeekendRequestDto;
import com.polezhaiev.avtodiva.dto.weekend.UpdateWeekendRequestDto;
import com.polezhaiev.avtodiva.dto.weekend.WeekendResponseDto;
import com.polezhaiev.avtodiva.dto.weekend.WeekendSearchParametersDto;
import com.polezhaiev.avtodiva.mapper.WeekendMapper;
import com.polezhaiev.avtodiva.service.weekend.WeekendService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/weekends")
public class WeekendController {
    private final WeekendService weekendService;
    private final WeekendMapper weekendMapper;

    @PostMapping
    public ResponseEntity<List<WeekendResponseDto>> createWeekend(@RequestBody @Valid List<CreateWeekendRequestDto> requestDto) {
        List<WeekendResponseDto> weekendResponseDto = weekendService.saveAll(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(weekendResponseDto);
    }

    @GetMapping("/search")
    public ResponseEntity<List<WeekendResponseDto>> searchWeekends(WeekendSearchParametersDto searchParameters) {
        List<WeekendResponseDto> response = weekendService.searchWeekends(searchParameters);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WeekendResponseDto> getWeekendById(@PathVariable Long id) {
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
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteWeekendById(@PathVariable Long id) {
        weekendService.deleteById(id);
    }
}
