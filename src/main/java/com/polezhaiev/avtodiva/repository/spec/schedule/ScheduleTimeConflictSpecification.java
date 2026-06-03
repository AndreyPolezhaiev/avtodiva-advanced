package com.polezhaiev.avtodiva.repository.spec.schedule;

import com.polezhaiev.avtodiva.model.ScheduleSlot;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

public class ScheduleTimeConflictSpecification {
    @SuppressWarnings("ConstantConditions")
    public static Specification<ScheduleSlot> getSpecification() {
        return (root, query, criteriaBuilder) -> {
            Subquery<Long> subquery = query.subquery(Long.class);
            Root<ScheduleSlot> subRoot = subquery.from(ScheduleSlot.class);
            subquery.select(subRoot.get("id"));

            Predicate excludeSelf = criteriaBuilder.notEqual(subRoot.get("id"), root.get("id"));

            Predicate isBooked = criteriaBuilder.equal(subRoot.get("booked"), true);

            Predicate sameDate = criteriaBuilder.equal(subRoot.get("date"), root.get("date"));

            Predicate sameInstructor = criteriaBuilder.equal(subRoot.get("instructor"), root.get("instructor"));
            Predicate sameCar = criteriaBuilder.equal(subRoot.get("car"), root.get("car"));
            Predicate resourceConflict = criteriaBuilder.or(sameInstructor, sameCar);

            Predicate timeOverlap = criteriaBuilder.and(
                    criteriaBuilder.lessThan(subRoot.get("startTime"), root.get("endTime")),
                    criteriaBuilder.greaterThan(subRoot.get("endTime"), root.get("startTime"))
            );

            subquery.where(excludeSelf, isBooked, sameDate, resourceConflict, timeOverlap);

            return criteriaBuilder.not(criteriaBuilder.exists(subquery));
        };
    }
}