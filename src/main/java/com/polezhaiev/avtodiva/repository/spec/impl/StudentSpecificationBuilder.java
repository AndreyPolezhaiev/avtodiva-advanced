package com.polezhaiev.avtodiva.repository.spec.impl;

import com.polezhaiev.avtodiva.dto.student.StudentSearchParametersDto;
import com.polezhaiev.avtodiva.model.Student;
import com.polezhaiev.avtodiva.repository.spec.SpecificationBuilder;
import org.springframework.data.jpa.domain.Specification;

public class StudentSpecificationBuilder implements SpecificationBuilder<Student, StudentSearchParametersDto> {
    @Override
    public Specification<Student> build(StudentSearchParametersDto searchParameters) {
        return null;
    }
}
