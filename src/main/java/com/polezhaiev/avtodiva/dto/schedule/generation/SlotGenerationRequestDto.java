package com.polezhaiev.avtodiva.dto.schedule.generation;

import lombok.Data;
import java.util.List;

@Data
public class SlotGenerationRequestDto {
    List<Long> instructorIds;
    List<Long> carIds;
    private int days;
}
