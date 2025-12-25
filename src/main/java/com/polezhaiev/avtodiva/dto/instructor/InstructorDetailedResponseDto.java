package com.polezhaiev.avtodiva.dto.instructor;

import com.polezhaiev.avtodiva.model.ScheduleSlot;
import com.polezhaiev.avtodiva.model.Weekend;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Accessors(chain = true)
@Data
public class InstructorDetailedResponseDto {
    private Long id;
    private String name;

    // TO.DO
//    private List<WeekendResponseDto> weekends;
//    private List<ScheduleSlotResponseDto> slots;
}
