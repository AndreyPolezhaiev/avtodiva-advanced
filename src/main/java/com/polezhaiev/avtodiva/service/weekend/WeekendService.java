package com.polezhaiev.avtodiva.service.weekend;

import com.polezhaiev.avtodiva.dto.weekend.CreateWeekendRequestDto;
import com.polezhaiev.avtodiva.dto.weekend.UpdateWeekendRequestDto;
import com.polezhaiev.avtodiva.dto.weekend.WeekendResponseDto;
import com.polezhaiev.avtodiva.mapper.WeekendMapper;
import com.polezhaiev.avtodiva.model.Instructor;
import com.polezhaiev.avtodiva.model.Weekend;
import com.polezhaiev.avtodiva.repository.InstructorRepository;
import com.polezhaiev.avtodiva.repository.WeekendRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class WeekendService {
    private final WeekendRepository weekendRepository;
    private final InstructorRepository instructorRepository;
    private final WeekendMapper weekendMapper;

    public WeekendResponseDto save(CreateWeekendRequestDto requestDto) {
        Instructor instructor = instructorRepository.findById(requestDto.getInstructorId()).orElseThrow(
                () -> new RuntimeException("Can't find instructor by id: " + requestDto.getInstructorId())
        );

        Weekend weekend = weekendMapper.toModel(requestDto);
        weekend.setInstructor(instructor);
        Weekend saved = weekendRepository.save(weekend);

        return weekendMapper.toResponseDto(saved);
    }

    public List<WeekendResponseDto> findAll() {
        return weekendRepository.findAll()
                .stream()
                .map(weekendMapper::toResponseDto)
                .toList();
    }

    public WeekendResponseDto findById(Long id) {
        Weekend weekendFromRepo = weekendRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Can't find weekend by id: " + id)
        );

        return weekendMapper.toResponseDto(weekendFromRepo);
    }

    public void deleteById(Long id) {
        Weekend weekendFromRepo = weekendRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Can't find weekend by id: " + id)
        );

        weekendRepository.delete(weekendFromRepo);
    }

    public WeekendResponseDto updateById(Long id, UpdateWeekendRequestDto requestDto) {
        Weekend weekendFromRepo = weekendRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Can't find weekend by id: " + id)
        );

        weekendFromRepo.setDay(requestDto.getDay());
        weekendFromRepo.setTimeFrom(requestDto.getTimeFrom());
        weekendFromRepo.setTimeTo(requestDto.getTimeTo());

        Weekend saved = weekendRepository.save(weekendFromRepo);

        return weekendMapper.toResponseDto(saved);
    }
}
