package com.polezhaiev.avtodiva.mapper;

import com.polezhaiev.avtodiva.config.MapperConfig;
import com.polezhaiev.avtodiva.dto.instructor.CreateInstructorRequestDto;
import com.polezhaiev.avtodiva.dto.instructor.InstructorDetailedResponseDto;
import com.polezhaiev.avtodiva.dto.instructor.InstructorResponseDto;
import com.polezhaiev.avtodiva.model.Instructor;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class, uses = WeekendMapper.class)
public interface InstructorMapper {
    InstructorResponseDto toResponseDto(Instructor instructor);
    InstructorDetailedResponseDto toDetailedResponseDto(Instructor instructor);
    Instructor toModel(CreateInstructorRequestDto requestDto);
}
