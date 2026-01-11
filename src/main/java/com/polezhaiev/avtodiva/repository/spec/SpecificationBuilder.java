package com.polezhaiev.avtodiva.repository.spec;

import com.polezhaiev.avtodiva.dto.schedule.SlotSearchParametersDto;
import org.springframework.data.jpa.domain.Specification;

public interface SpecificationBuilder<T> {
    Specification<T> build(SlotSearchParametersDto searchParameters);
}
