package com.polezhaiev.avtodiva.service.weekend;

import com.polezhaiev.avtodiva.dto.weekend.CreateWeekendRequestDto;
import com.polezhaiev.avtodiva.dto.weekend.UpdateWeekendRequestDto;
import com.polezhaiev.avtodiva.dto.weekend.WeekendResponseDto;
import com.polezhaiev.avtodiva.mapper.WeekendMapper;
import com.polezhaiev.avtodiva.model.Instructor;
import com.polezhaiev.avtodiva.model.Weekend;
import com.polezhaiev.avtodiva.repository.InstructorRepository;
import com.polezhaiev.avtodiva.repository.WeekendRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WeekendService {
    private final WeekendRepository weekendRepository;
    private final InstructorRepository instructorRepository;
    private final WeekendMapper weekendMapper;

    public List<WeekendResponseDto> saveAll(List<CreateWeekendRequestDto> requestDto) {
        Instructor instructor = instructorRepository.findById(requestDto.getFirst().getInstructorId()).orElseThrow(
                () -> new RuntimeException("Can't find instructor by id: " + requestDto.getFirst().getInstructorId())
        );

        List<Weekend> weekends = requestDto.stream()
                .map(dto -> {
                    Weekend weekend = weekendMapper.toModel(dto);
                    weekend.setInstructor(instructor);
                    return weekend;
                })
                .toList();

        List<Weekend> saved = weekendRepository.saveAll(weekends);

        return weekendMapper.toResponseDtoList(saved);
    }

    public List<WeekendResponseDto> findAll() {
        List<Weekend> weekends = weekendRepository.findAllWithInstructor();

        return weekendMapper.toResponseDtoList(weekends);
    }

    public WeekendResponseDto findById(Long id) {
        Weekend weekendFromRepo = weekendRepository.findByIdWithInstructor(id).orElseThrow(
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
        Weekend weekendFromRepo = weekendRepository.findByIdWithInstructor(id).orElseThrow(
                () -> new RuntimeException("Can't find weekend by id: " + id)
        );

        weekendFromRepo.setDate(requestDto.getDate());
        weekendFromRepo.setStartTime(requestDto.getStartTime());
        weekendFromRepo.setEndTime(requestDto.getEndTime());

        Weekend saved = weekendRepository.save(weekendFromRepo);

        return weekendMapper.toResponseDto(saved);
    }
}
