package com.polezhaiev.avtodiva.controller;

import com.polezhaiev.avtodiva.dto.student.CreateStudentRequestDto;
import com.polezhaiev.avtodiva.dto.student.StudentResponseDto;
import com.polezhaiev.avtodiva.dto.student.UpdateStudentRequestDto;
import com.polezhaiev.avtodiva.service.student.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/students")
public class StudentController {
    private final StudentService studentService;

    @PostMapping
    public ResponseEntity<StudentResponseDto> createStudent(@RequestBody @Valid CreateStudentRequestDto requestDto) {
        StudentResponseDto studentResponseDto = studentService.save(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(studentResponseDto);
    }

    @GetMapping("/all")
    public ResponseEntity<List<StudentResponseDto>> getAllStudents() {
        List<StudentResponseDto> allStudents = studentService.findAll();

        return ResponseEntity.status(HttpStatus.OK).body(allStudents);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentResponseDto> getStudentById(@PathVariable Long id) {
        StudentResponseDto studentById = studentService.findById(id);

        return ResponseEntity.status(HttpStatus.OK).body(studentById);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentResponseDto> updateStudentById(@PathVariable Long id,
                                                                @RequestBody @Valid UpdateStudentRequestDto requestDto) {
        StudentResponseDto studentResponseDto = studentService.updateById(id, requestDto);

        return ResponseEntity.status(HttpStatus.OK).body(studentResponseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStudentById(@PathVariable Long id) {
        studentService.deleteById(id);
        String response = "Student by id " + id + " was successfully removed";
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
