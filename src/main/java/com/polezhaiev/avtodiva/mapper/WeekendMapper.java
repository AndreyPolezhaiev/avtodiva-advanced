package com.polezhaiev.avtodiva.mapper;

import com.polezhaiev.avtodiva.config.MapperConfig;
import com.polezhaiev.avtodiva.dto.instructor.InstructorResponseDto;
import com.polezhaiev.avtodiva.dto.weekend.CreateWeekendRequestDto;
import com.polezhaiev.avtodiva.dto.weekend.WeekendResponseDto;
import com.polezhaiev.avtodiva.model.Weekend;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface WeekendMapper {
    @Mapping(target = "instructorDto", ignore = true)
    WeekendResponseDto toResponseDto(Weekend weekend);

    @AfterMapping
    default void setInstructorDto(@MappingTarget WeekendResponseDto dto, Weekend weekend) {
        InstructorResponseDto instructorDto = new InstructorResponseDto()
                .setId(weekend.getInstructor().getId())
                .setName(weekend.getInstructor().getName());

        dto.setInstructorDto(instructorDto);
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "instructor", ignore = true)
    Weekend toModel(CreateWeekendRequestDto requestDto);
}
