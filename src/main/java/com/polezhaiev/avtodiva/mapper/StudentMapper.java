package com.polezhaiev.avtodiva.mapper;

import com.polezhaiev.avtodiva.config.MapperConfig;
import com.polezhaiev.avtodiva.dto.student.CreateStudentRequestDto;
import com.polezhaiev.avtodiva.dto.student.StudentResponseDto;
import com.polezhaiev.avtodiva.model.Student;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface StudentMapper {
    StudentResponseDto toResponseDto(Student student);
    Student toModel(CreateStudentRequestDto requestDto);
}
