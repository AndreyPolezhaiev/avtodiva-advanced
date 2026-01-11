package com.polezhaiev.avtodiva.repository.spec.schedule;

import com.polezhaiev.avtodiva.model.ScheduleSlot;
import org.springframework.data.jpa.domain.Specification;

public class StudentSpecification {
    public static Specification<ScheduleSlot> getSpecification(Long studentId) {
        return (root, query, cb) ->
                studentId == null
                    ? null
                    : cb.equal(root.get("student").get("id"), studentId);
    }
}
