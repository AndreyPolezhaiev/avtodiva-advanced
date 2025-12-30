package com.polezhaiev.avtodiva.mapper;

import com.polezhaiev.avtodiva.config.MapperConfig;
import com.polezhaiev.avtodiva.dto.weekend.CreateWeekendRequestDto;
import com.polezhaiev.avtodiva.dto.weekend.WeekendResponseDto;
import com.polezhaiev.avtodiva.model.Weekend;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface WeekendMapper {
    @Mapping(target = "instructorId", source = "instructor.id")
    WeekendResponseDto toResponseDto(Weekend weekend);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "instructor", ignore = true)
    Weekend toModel(CreateWeekendRequestDto requestDto);
}
