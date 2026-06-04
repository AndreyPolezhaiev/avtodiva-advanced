package com.polezhaiev.avtodiva.controller;

import com.polezhaiev.avtodiva.dto.schedule.generation.SlotGenerationRequestDto;
import com.polezhaiev.avtodiva.service.schedule.SlotGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/slot-generator")
public class SlotGeneratorController {
    private final SlotGeneratorService slotGeneratorService;

    @PostMapping("/generate")
    public ResponseEntity<String> generateSlots(@RequestBody SlotGenerationRequestDto requestDto) {
        slotGeneratorService.addFreeWindowsForEachInstructor(requestDto);
        return ResponseEntity.ok("Successfully generated slots for " + requestDto.getDays() + " days.");
    }
}
