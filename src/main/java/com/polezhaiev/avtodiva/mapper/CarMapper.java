package com.polezhaiev.avtodiva.mapper;

import com.polezhaiev.avtodiva.config.MapperConfig;
import com.polezhaiev.avtodiva.dto.car.CarDto;
import com.polezhaiev.avtodiva.dto.car.CarResponseDto;
import com.polezhaiev.avtodiva.dto.car.CreateCarRequestDto;
import com.polezhaiev.avtodiva.model.Car;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface CarMapper {
    CarResponseDto toResponseDto(Car car);
    Car toModel(CarDto dto);
    CarDto toDto(CreateCarRequestDto requestDto);
}