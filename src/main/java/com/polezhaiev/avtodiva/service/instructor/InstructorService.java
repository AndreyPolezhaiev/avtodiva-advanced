package com.polezhaiev.avtodiva.service.instructor;

import com.polezhaiev.avtodiva.model.Instructor;
import com.polezhaiev.avtodiva.repository.InstructorRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class InstructorService {
    private final InstructorRepository instructorRepository;

    @Cacheable("instructors")
    public List<Instructor> findAllInstructors() {
        return instructorRepository.findAll();
    }

    @Caching(evict = {
            @CacheEvict(value = "instructors", allEntries = true),
            @CacheEvict(value = "instructorNames", allEntries = true),
            @CacheEvict(value = "instructorByName", key = "#instructor.name")
    })
    public void saveInstructor(Instructor instructor) {
        if (instructor == null || instructor.getName() == null || instructor.getName().isBlank()) {
            throw new IllegalArgumentException("Ім'я інструктора не може бути порожнім!");
        }

        if (instructorRepository.existsByNameIgnoreCase(instructor.getName())) {
            throw new IllegalStateException("Інструктор з іменем '" + instructor.getName() + "' вже існує!");
        }
        instructor.setName(instructor.getName());
        instructorRepository.save(instructor);
    }

    public void saveAllInstructors(List<Instructor> instructors) {
        instructorRepository.saveAll(instructors);
    }

    public Instructor findById(Long id) {
        return instructorRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Can't find instructor by id: " + id)
        );
    }

    @Cacheable("instructorByName")
    public Instructor findByName(String name) {
        return instructorRepository.findByName(name).orElseThrow(
                () -> new RuntimeException("Can't find instructor by name: " + name)
        );
    }

    @Cacheable("instructorNames")
    public String[] getInstructorsNames() {
        List<String> names = instructorRepository.findAllInstructorNames();
        return names.toArray(new String[0]);
    }

    @Caching(evict = {
            @CacheEvict(value = "instructors", allEntries = true),
            @CacheEvict(value = "instructorNames", allEntries = true),
            @CacheEvict(value = "instructorByName", key = "#name")
    })
    @Transactional
    public void deleteByName(String name) {
        instructorRepository.deleteByName(name);
    }
}
