package com.polezhaiev.avtodiva.service.student;

import com.polezhaiev.avtodiva.dto.student.CreateStudentRequestDto;
import com.polezhaiev.avtodiva.dto.student.StudentResponseDto;
import com.polezhaiev.avtodiva.dto.student.UpdateStudentRequestDto;
import com.polezhaiev.avtodiva.mapper.StudentMapper;
import com.polezhaiev.avtodiva.model.Student;
import com.polezhaiev.avtodiva.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    public StudentResponseDto save(CreateStudentRequestDto requestDto) {
        String cleanPhone = requestDto.getPhoneNumber().replaceAll("\\D", "");
        requestDto.setPhoneNumber(cleanPhone);

        if (studentRepository.existsByPhoneNumber(
                requestDto.getPhoneNumber())
        ) {
            throw new IllegalStateException(
                    "Student with phone number '" + requestDto.getPhoneNumber() + "' already exists!"
            );
        }

        Student student = studentMapper.toModel(requestDto);
        student.setScheduleSlots(List.of());

        Student saved = studentRepository.save(student);
        
        return studentMapper.toResponseDto(saved);
    }

    public List<StudentResponseDto> findAll() {
        return studentRepository.findAll()
                .stream()
                .map(studentMapper::toResponseDto)
                .toList();
    }

    public StudentResponseDto findById(Long id) {
        Student studentFromRepo = studentRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Can't find student by id: " + id)
        );

        return studentMapper.toResponseDto(studentFromRepo);
    }

    public List<StudentResponseDto> findAllByName(String name) {
        return studentRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(studentMapper::toResponseDto)
                .toList();
    }

    public StudentResponseDto findByPhoneNumber(String phoneNumber) {
        return studentRepository.findByPhoneNumber(phoneNumber)
                .map(studentMapper::toResponseDto)
                .orElse(null);
    }

    @Transactional
    public void deleteById(Long id) {
        Student studentFromRepo = studentRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Can't find student by id: " + id)
        );

        studentRepository.delete(studentFromRepo);
    }

    public StudentResponseDto updateById(Long id, UpdateStudentRequestDto requestDto) {
        Student studentFromRepo = studentRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Can't find student by id: " + id)
        );

        String cleanPhone = requestDto.getPhoneNumber().replaceAll("\\D", "");
        requestDto.setPhoneNumber(cleanPhone);

        boolean isNameChanged = !studentFromRepo.getName().equalsIgnoreCase(requestDto.getName());
        boolean isPhoneChanged = (studentFromRepo.getPhoneNumber() == null && requestDto.getPhoneNumber() != null) ||
                (studentFromRepo.getPhoneNumber() != null && !studentFromRepo.getPhoneNumber().equals(requestDto.getPhoneNumber()));

        if (isNameChanged || isPhoneChanged) {
            if (studentRepository.existsByPhoneNumber(requestDto.getPhoneNumber())) {
                throw new IllegalStateException(
                        "Student with phone number: " + requestDto.getPhoneNumber() + " already exists!"
                );
            }
        }

        studentFromRepo.setName(requestDto.getName());
        studentFromRepo.setPhoneNumber(requestDto.getPhoneNumber());

        Student saved = studentRepository.save(studentFromRepo);
        return studentMapper.toResponseDto(saved);
    }
}
