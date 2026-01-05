package com.polezhaiev.avtodiva.mapper;

import com.polezhaiev.avtodiva.config.MapperConfig;
import com.polezhaiev.avtodiva.dto.schedule.CreateScheduleSlotRequestDto;
import com.polezhaiev.avtodiva.dto.schedule.ScheduleSlotResponseDto;
import com.polezhaiev.avtodiva.model.ScheduleSlot;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class, uses = {CarMapper.class, InstructorMapper.class, StudentMapper.class})
public interface ScheduleSlotMapper {
    ScheduleSlot toModel(CreateScheduleSlotRequestDto requestDto);
    ScheduleSlotResponseDto toResponseDto(ScheduleSlot scheduleSlot);
}
