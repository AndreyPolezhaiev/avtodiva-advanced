package com.polezhaiev.avtodiva.service.instructor;

import com.polezhaiev.avtodiva.dto.instructor.InstructorDetailedResponseDto;
import com.polezhaiev.avtodiva.dto.instructor.InstructorDto;
import com.polezhaiev.avtodiva.dto.instructor.InstructorResponseDto;
import com.polezhaiev.avtodiva.mapper.InstructorMapper;
import com.polezhaiev.avtodiva.model.Instructor;
import com.polezhaiev.avtodiva.repository.InstructorRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class InstructorService {
    private final InstructorRepository instructorRepository;
    private final InstructorMapper instructorMapper;

    public InstructorResponseDto save(InstructorDto dto) {
        if (instructorRepository.existsByNameIgnoreCase(dto.getName())) {
            throw new IllegalStateException("Instructor with name '" + dto.getName() + "' already exists!");
        }

        Instructor instructor = instructorMapper.toModel(dto);
        instructor.setSlots(new ArrayList<>());
        instructor.setWeekends(new ArrayList<>());

        Instructor savedInstructor = instructorRepository.save(instructor);

        return instructorMapper.toResponseDto(savedInstructor);
    }

    public List<InstructorResponseDto> findAll() {
        return instructorRepository.findAll()
                .stream()
                .map(instructorMapper::toResponseDto)
                .toList();
    }

    public InstructorResponseDto findById(Long id) {
        Instructor instructorFromRepo = instructorRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Can't find instructor by id: " + id)
        );

        return instructorMapper.toResponseDto(instructorFromRepo);
    }

    public InstructorDetailedResponseDto findDetailedById(Long id) {
        Instructor instructorFromRepo = instructorRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Can't find instructor by id: " + id)
        );

        return instructorMapper.toDetailedResponseDto(instructorFromRepo);
    }

    public void deleteById(Long id) {
        Instructor instructorFromRepo = instructorRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Can't find instructor by id: " + id)
        );

        instructorRepository.delete(instructorFromRepo);
    }

    public InstructorResponseDto updateNameById(Long id, InstructorDto dto) {
        Instructor instructorFromRepo = instructorRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Can't find instructor by id: " + id)
        );

        instructorFromRepo.setName(dto.getName());

        Instructor updatedInstructor = instructorRepository.save(instructorFromRepo);

        return instructorMapper.toResponseDto(updatedInstructor);
    }
}
