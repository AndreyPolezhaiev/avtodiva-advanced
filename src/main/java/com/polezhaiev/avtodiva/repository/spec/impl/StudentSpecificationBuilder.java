package com.polezhaiev.avtodiva.repository.spec.impl;

import com.polezhaiev.avtodiva.dto.student.StudentSearchParametersDto;
import com.polezhaiev.avtodiva.model.Student;
import com.polezhaiev.avtodiva.repository.spec.SpecificationBuilder;
import com.polezhaiev.avtodiva.repository.spec.student.NameSpecification;
import com.polezhaiev.avtodiva.repository.spec.student.PhoneSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class StudentSpecificationBuilder implements SpecificationBuilder<Student, StudentSearchParametersDto> {
    @Override
    public Specification<Student> build(StudentSearchParametersDto searchParameters) {
        Specification<Student> spec = Specification.allOf();

        if (searchParameters.getName() != null && !searchParameters.getName().isBlank()) {
            spec = spec.and(NameSpecification.getSpecification(searchParameters.getName()));
        }

        if (searchParameters.getPhoneNumber() != null && !searchParameters.getPhoneNumber().isBlank()) {
            spec = spec.and(PhoneSpecification.getSpecification(searchParameters.getPhoneNumber()));
        }

        return spec;
    }
}
