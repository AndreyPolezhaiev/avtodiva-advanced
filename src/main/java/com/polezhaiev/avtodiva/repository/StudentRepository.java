package com.polezhaiev.avtodiva.repository;

import com.polezhaiev.avtodiva.model.Student;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    boolean existsByNameIgnoreCase(String name);
}
