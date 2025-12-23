package com.polezhaiev.avtodiva.repository;

import com.polezhaiev.avtodiva.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    Optional<Car> findByName(String name);
    boolean existsByNameIgnoreCase(String name);
}