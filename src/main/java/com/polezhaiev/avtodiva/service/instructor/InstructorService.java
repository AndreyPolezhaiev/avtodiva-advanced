package com.polezhaiev.avtodiva.service.instructor;


import com.polezhaiev.avtodiva.model.Instructor;

import java.util.List;

public interface InstructorService {
    List<Instructor> findAllInstructors();
    void saveInstructor(Instructor instructor);
    void saveAllInstructors(List<Instructor> list);
    Instructor findById(Long id);
    Instructor findByName(String name);
    String[] getInstructorsNames();
    void deleteByName(String name);
}
