package com.polezhaiev.avtodiva.repository.spec.schedule;

import com.polezhaiev.avtodiva.model.ScheduleSlot;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class DateRangeSpecification {
    public static Specification<ScheduleSlot> getSpecification(LocalDate from, LocalDate to) {
        return (root, query, cb) -> {
            if (from == null && to == null) return null;
            if (from != null && to != null) return cb.between(root.get("date"), from, to);
            if (from != null) return cb.greaterThanOrEqualTo(root.get("date"), from);
            return cb.lessThanOrEqualTo(root.get("date"), to);
        };
    }
}
