package com.polezhaiev.avtodiva.repository.spec.schedule;

import com.polezhaiev.avtodiva.model.ScheduleSlot;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class CarSpecification {
    public static Specification<ScheduleSlot> getSpecification(List<Long> carIds) {
        return (root, query, cb) ->
                (carIds == null || carIds.isEmpty())
                    ? null
                    : root.get("car").get("id").in(carIds);
    }
}
