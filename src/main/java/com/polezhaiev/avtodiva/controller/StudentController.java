package com.polezhaiev.avtodiva.controller;

import com.polezhaiev.avtodiva.dto.student.CreateStudentRequestDto;
import com.polezhaiev.avtodiva.dto.student.StudentResponseDto;
import com.polezhaiev.avtodiva.dto.student.StudentSearchParametersDto;
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

    @GetMapping("/search")
    public ResponseEntity<List<StudentResponseDto>> searchStudents(StudentSearchParametersDto searchParameters) {
        List<StudentResponseDto> response = studentService.searchStudents(searchParameters);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentResponseDto> getStudentById(@PathVariable Long id) {
        StudentResponseDto studentById = studentService.findById(id);

        return ResponseEntity.status(HttpStatus.OK).body(studentById);
    }

    @GetMapping("/search")
    public ResponseEntity<List<StudentResponseDto>> searchStudents(@RequestParam String name) {
        List<StudentResponseDto> students = studentService.findAllByName(name);
        return ResponseEntity.ok(students);
    }

    @GetMapping("/byPhone")
    public ResponseEntity<StudentResponseDto> findStudentByPhoneNumber(@RequestParam String phoneNumber) {
        StudentResponseDto studentByPhoneNumber = studentService.findByPhoneNumber(phoneNumber);
        return ResponseEntity.ok(studentByPhoneNumber);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentResponseDto> updateStudentById(@PathVariable Long id,
                                                                @RequestBody @Valid UpdateStudentRequestDto requestDto) {
        StudentResponseDto studentResponseDto = studentService.updateById(id, requestDto);

        return ResponseEntity.status(HttpStatus.OK).body(studentResponseDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStudentById(@PathVariable Long id) {
        studentService.deleteById(id);
    }
}
