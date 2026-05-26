package com.polezhaiev.avtodiva.service.schedule.template.time;

import com.polezhaiev.avtodiva.dto.template.time.ScheduleTemplateRequestDto;
import com.polezhaiev.avtodiva.dto.template.time.ScheduleTemplateResponseDto;
import com.polezhaiev.avtodiva.mapper.ScheduleTemplateMapper;
import com.polezhaiev.avtodiva.model.template.time.ScheduleTemplate;
import com.polezhaiev.avtodiva.repository.ScheduleTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ScheduleTemplateService {
    private final ScheduleTemplateRepository scheduleTemplateRepository;
    public final ScheduleTemplateMapper scheduleTemplateMapper;

    public ScheduleTemplateResponseDto save(ScheduleTemplateRequestDto requestDto) {
        ScheduleTemplate scheduleTemplate = scheduleTemplateMapper.toModel(requestDto);

        ScheduleTemplate saved = scheduleTemplateRepository.save(scheduleTemplate);

        return scheduleTemplateMapper.toResponseDto(saved);
    }

    public List<ScheduleTemplateResponseDto> findAll() {
        return scheduleTemplateRepository.findAll()
                .stream()
                .map(scheduleTemplateMapper::toResponseDto)
                .toList();
    }

    public ScheduleTemplateResponseDto updateById(Long id, ScheduleTemplateRequestDto requestDto) {
        ScheduleTemplate scheduleTemplate = scheduleTemplateRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Can't find ScheduleTemplate by id: " + id)
        );

        ScheduleTemplate requestTemplate = scheduleTemplateMapper.toModel(requestDto);

        scheduleTemplate.setIntervals(requestTemplate.getIntervals());

        ScheduleTemplate saved = scheduleTemplateRepository.save(scheduleTemplate);

        return scheduleTemplateMapper.toResponseDto(saved);
    }

    public void deleteById(Long id) {
        ScheduleTemplate scheduleTemplate = scheduleTemplateRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Can't find ScheduleTemplate by id: " + id)
        );

        scheduleTemplateRepository.delete(scheduleTemplate);
    }
}
