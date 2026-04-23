package com.polezhaiev.avtodiva.repository.spec.student;

import com.polezhaiev.avtodiva.model.Student;
import org.springframework.data.jpa.domain.Specification;

public class NameSpecification {
    public static Specification<Student> getSpecification(String name) {
        return (root, query, cb) ->
                (name == null || name.isBlank())
                        ? null
                        : cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase().trim() + "%");
    }
}
