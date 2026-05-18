package com.polezhaiev.avtodiva.mapper;

import com.polezhaiev.avtodiva.config.MapperConfig;
import com.polezhaiev.avtodiva.dto.template.time.ScheduleTemplateRequestDto;
import com.polezhaiev.avtodiva.dto.template.time.ScheduleTemplateResponseDto;
import com.polezhaiev.avtodiva.model.template.time.ScheduleTemplate;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface ScheduleTemplateMapper {
    ScheduleTemplateResponseDto toResponseDto(ScheduleTemplate scheduleTemplate);
    ScheduleTemplate toModel(ScheduleTemplateRequestDto requestDto);
}
