package com.polezhaiev.avtodiva.repository.spec.schedule;

import com.polezhaiev.avtodiva.model.ScheduleSlot;
import org.springframework.data.jpa.domain.Specification;

public class BookedSpecification {
    public static Specification<ScheduleSlot> getSpecification(Boolean booked) {
        return (root, query, cb) ->
                booked == null
                        ? null
                        : cb.equal(root.get("booked"), booked);
    }
}
