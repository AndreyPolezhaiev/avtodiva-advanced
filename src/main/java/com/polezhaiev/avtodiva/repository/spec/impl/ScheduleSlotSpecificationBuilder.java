package com.polezhaiev.avtodiva.repository.spec.impl;

import com.polezhaiev.avtodiva.dto.schedule.SlotSearchParametersDto;
import com.polezhaiev.avtodiva.model.ScheduleSlot;
import com.polezhaiev.avtodiva.repository.spec.SpecificationBuilder;
import com.polezhaiev.avtodiva.repository.spec.schedule.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class ScheduleSlotSpecificationBuilder implements SpecificationBuilder<ScheduleSlot> {
    @Override
    public Specification<ScheduleSlot> build(SlotSearchParametersDto searchParameters) {
        Specification<ScheduleSlot> spec = Specification.allOf();

        if (searchParameters.getBooked() != null) {
            spec = spec.and(BookedSpecification.getSpecification(searchParameters.getBooked()));
        }
        if (searchParameters.getCarIds() != null && !searchParameters.getCarIds().isEmpty()) {
            spec = spec.and(CarSpecification.getSpecification(searchParameters.getCarIds()));
        }
        if (searchParameters.getInstructorIds() != null && !searchParameters.getInstructorIds().isEmpty()) {
            spec = spec.and(InstructorSpecification.getSpecification(searchParameters.getInstructorIds()));
        }
        if (searchParameters.getStudentId() != null) {
            spec = spec.and(StudentSpecification.getSpecification(searchParameters.getStudentId()));
        }
        if (searchParameters.getDateFrom() != null || searchParameters.getDateTo() != null) {
            spec = spec.and(DateRangeSpecification.getSpecification(
                    searchParameters.getDateFrom(),
                    searchParameters.getDateTo()
            ));
        }

        return spec;
    }
}
