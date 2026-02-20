package com.polezhaiev.avtodiva.dto.instructor;

import com.polezhaiev.avtodiva.dto.schedule.ScheduleSlotResponseDto;
import com.polezhaiev.avtodiva.dto.weekend.WeekendResponseDto;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Accessors(chain = true)
@Data
public class InstructorDetailedResponseDto {
    private Long id;
    private String name;
    private List<WeekendResponseDto> weekends;
    private List<ScheduleSlotResponseDto> slots;
}
