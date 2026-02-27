package com.polezhaiev.avtodiva.controller;

import com.polezhaiev.avtodiva.dto.schedule.CreateScheduleSlotRequestDto;
import com.polezhaiev.avtodiva.dto.schedule.SlotSearchParametersDto;
import com.polezhaiev.avtodiva.dto.schedule.ScheduleSlotResponseDto;
import com.polezhaiev.avtodiva.dto.schedule.UpdateScheduleSlotRequestDto;
import com.polezhaiev.avtodiva.service.schedule.ScheduleSlotService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/schedule")
public class ScheduleSlotController {
    private final ScheduleSlotService scheduleSlotService;

    @PostMapping
    public ResponseEntity<ScheduleSlotResponseDto> createSlot(@RequestBody @Valid CreateScheduleSlotRequestDto requestDto) {
        ScheduleSlotResponseDto response = scheduleSlotService.create(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ScheduleSlotResponseDto>> searchSlots(@Valid SlotSearchParametersDto requestDto) {
        List<ScheduleSlotResponseDto> response = scheduleSlotService.searchSlots(requestDto);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScheduleSlotResponseDto> getSlotById(@PathVariable Long id) {
        ScheduleSlotResponseDto response = scheduleSlotService.findById(id);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ScheduleSlotResponseDto> updateSlotById(@PathVariable Long id,
                                                                  @RequestBody @Valid UpdateScheduleSlotRequestDto requestDto) {
        ScheduleSlotResponseDto response = scheduleSlotService.updateById(id, requestDto);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSlotById(@PathVariable Long id) {
        scheduleSlotService.deleteById(id);
    }
}
