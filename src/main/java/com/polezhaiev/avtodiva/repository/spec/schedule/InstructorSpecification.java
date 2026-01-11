package com.polezhaiev.avtodiva.repository.spec.schedule;

import com.polezhaiev.avtodiva.model.ScheduleSlot;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class InstructorSpecification {
    public static Specification<ScheduleSlot> getSpecification(List<Long> instructorIds) {
        return (root, query, cb) ->
                (instructorIds == null || instructorIds.isEmpty())
                    ? null
                    : root.get("instructor").get("id").in(instructorIds);
    }
}
