package com.polezhaiev.avtodiva.repository.spec.weekend;

import com.polezhaiev.avtodiva.model.Weekend;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class WeekendInstructorSpecification {
    public static Specification<Weekend> getSpecification(List<Long> instructorIds) {
        return (root, query, cb) ->
                (instructorIds == null || instructorIds.isEmpty())
                        ? null
                        : root.get("instructor").get("id").in(instructorIds);
    }
}
