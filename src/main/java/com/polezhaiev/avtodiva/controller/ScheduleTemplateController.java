package com.polezhaiev.avtodiva.controller;

import com.polezhaiev.avtodiva.dto.template.time.ScheduleTemplateRequestDto;
import com.polezhaiev.avtodiva.dto.template.time.ScheduleTemplateResponseDto;
import com.polezhaiev.avtodiva.service.schedule.template.time.ScheduleTemplateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/template")
public class ScheduleTemplateController {
    private final ScheduleTemplateService scheduleTemplateService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ScheduleTemplateResponseDto> createTemplate(
            @RequestBody @Valid ScheduleTemplateRequestDto requestDto) {
        ScheduleTemplateResponseDto response = scheduleTemplateService.save(requestDto);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ScheduleTemplateResponseDto>> getAllTemplates() {
        List<ScheduleTemplateResponseDto> response = scheduleTemplateService.findAll();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ScheduleTemplateResponseDto> updateTemplateById(
            @PathVariable Long id,
            @RequestBody @Valid ScheduleTemplateRequestDto requestDto) {

        ScheduleTemplateResponseDto response = scheduleTemplateService.updateById(id, requestDto);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTemplateById(@PathVariable Long id) {
        scheduleTemplateService.deleteById(id);
    }
}
