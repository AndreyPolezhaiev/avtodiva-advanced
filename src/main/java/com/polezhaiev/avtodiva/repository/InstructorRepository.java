package com.polezhaiev.avtodiva.repository;

import com.polezhaiev.avtodiva.model.Instructor;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InstructorRepository extends JpaRepository<Instructor, Long> {
    boolean existsByNameIgnoreCase(String name);

    @Query("SELECT i FROM Instructor i")
    @EntityGraph(attributePaths = {"scheduleTemplate", "scheduleTemplate.intervals"})
    List<Instructor> findAllWithIntervalsById(@Param("ids") List<Long> ids);

    @Query("SELECT i FROM Instructor i WHERE i.id IN :ids")
    @EntityGraph(attributePaths = {"scheduleTemplate", "scheduleTemplate.intervals"})
    List<Instructor> findAllWithIntervals();
}