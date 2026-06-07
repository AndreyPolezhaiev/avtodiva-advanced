package com.polezhaiev.avtodiva.controller;

import com.polezhaiev.avtodiva.dto.instructor.CreateInstructorRequestDto;
import com.polezhaiev.avtodiva.dto.instructor.InstructorDetailedResponseDto;
import com.polezhaiev.avtodiva.dto.instructor.InstructorResponseDto;
import com.polezhaiev.avtodiva.mapper.InstructorMapper;
import com.polezhaiev.avtodiva.service.instructor.InstructorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/instructors")
public class InstructorController {
    private final InstructorService instructorService;
    private final InstructorMapper instructorMapper;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<InstructorResponseDto> createInstructor(
            @RequestBody @Valid CreateInstructorRequestDto requestDto) {
        InstructorResponseDto instructorResponseDto = instructorService.save(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(instructorResponseDto);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<InstructorResponseDto>> getAllInstructors() {
        List<InstructorResponseDto> allInstructors = instructorService.findAll();

        return ResponseEntity.status(HttpStatus.OK).body(allInstructors);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<InstructorResponseDto> getInstructorById(@PathVariable Long id) {
        InstructorResponseDto instructorById = instructorService.findById(id);

        return ResponseEntity.status(HttpStatus.OK).body(instructorById);
    }

    @GetMapping("/{id}/details")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<InstructorDetailedResponseDto> getDetailedInstructorById(@PathVariable Long id) {
        InstructorDetailedResponseDto instructorById = instructorService.findDetailedById(id);

        return ResponseEntity.status(HttpStatus.OK).body(instructorById);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<InstructorResponseDto> updateInstructorById(@PathVariable Long id,
                                                                          @RequestBody @Valid CreateInstructorRequestDto requestDto) {
        InstructorResponseDto instructorResponseDto = instructorService.updateById(id, requestDto);

        return ResponseEntity.status(HttpStatus.OK).body(instructorResponseDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteInstructorById(@PathVariable Long id) {
        instructorService.deleteById(id);
    }
}
