package com.polezhaiev.avtodiva.service.student;

import com.polezhaiev.avtodiva.dto.student.CreateStudentRequestDto;
import com.polezhaiev.avtodiva.dto.student.StudentResponseDto;
import com.polezhaiev.avtodiva.dto.student.UpdateStudentRequestDto;
import com.polezhaiev.avtodiva.mapper.StudentMapper;
import com.polezhaiev.avtodiva.model.ScheduleSlot;
import com.polezhaiev.avtodiva.model.Student;
import com.polezhaiev.avtodiva.repository.ScheduleSlotRepository;
import com.polezhaiev.avtodiva.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final ScheduleSlotRepository scheduleSlotRepository;
    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    public StudentResponseDto save(CreateStudentRequestDto requestDto, List<ScheduleSlot> scheduleSlots) {
        if (studentRepository.existsByNameIgnoreCase(requestDto.getName())) {
            throw new IllegalStateException("Student with name '" + requestDto.getName() + "' already exists!");
        }

        Student student = studentMapper.toModel(requestDto);
        student.setName(requestDto.getName());
        student.setScheduleSlots(scheduleSlots);

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

        studentFromRepo.setName(requestDto.getName());

        Student saved = studentRepository.save(studentFromRepo);
        return studentMapper.toResponseDto(saved);
    }

    public List<String> getStudentsNames() {
        return scheduleSlotRepository.findDistinctStudentNames();
    }
}
