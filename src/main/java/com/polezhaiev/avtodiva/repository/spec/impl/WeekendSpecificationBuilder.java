package com.polezhaiev.avtodiva.repository.spec.impl;

import com.polezhaiev.avtodiva.dto.weekend.WeekendSearchParametersDto;
import com.polezhaiev.avtodiva.model.Weekend;
import com.polezhaiev.avtodiva.repository.spec.SpecificationBuilder;
import com.polezhaiev.avtodiva.repository.spec.schedule.*;
import com.polezhaiev.avtodiva.repository.spec.weekend.WeekendDateRangeSpecification;
import com.polezhaiev.avtodiva.repository.spec.weekend.WeekendInstructorSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class WeekendSpecificationBuilder implements SpecificationBuilder<Weekend, WeekendSearchParametersDto>  {
    @Override
    public Specification<Weekend> build(WeekendSearchParametersDto searchParameters) {
        Specification<Weekend> spec = Specification.allOf();

        if (searchParameters.getInstructorIds() != null && !searchParameters.getInstructorIds().isEmpty()) {
            spec = spec.and(WeekendInstructorSpecification.getSpecification(searchParameters.getInstructorIds()));
        }

        if (searchParameters.getStartDate() != null || searchParameters.getEndDate() != null) {
            spec = spec.and(WeekendDateRangeSpecification.getSpecification(
                    searchParameters.getStartDate(),
                    searchParameters.getEndDate()
            ));
        }

        return spec;
    }
}
