package com.polezhaiev.avtodiva.repository.spec.student;

import com.polezhaiev.avtodiva.model.Student;
import org.springframework.data.jpa.domain.Specification;

public class PhoneSpecification {
    public static Specification<Student> getSpecification(String phoneNumber) {
        return (root, query, cb) -> {
            if (phoneNumber == null || phoneNumber.isBlank()) {
                return null;
            }

            String cleanPhone = phoneNumber.replaceAll("\\D", "");

            return cb.like(
                    cb.lower(root.get("phone_number")),
                    "%" + cleanPhone + "%"
            );
        };
    }
}
