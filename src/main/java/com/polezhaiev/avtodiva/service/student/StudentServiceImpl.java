package com.polezhaiev.avtodiva.service.student;

import com.polezhaiev.avtodiva.repository.ScheduleSlotRepository;
import com.polezhaiev.avtodiva.service.student.StudentService;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final ScheduleSlotRepository scheduleSlotRepository;

    // students that are being taught and belong to at least one slot
    @Override
    @Cacheable("studentNames")
    public String[] getStudentsNames() {
        List<String> names = scheduleSlotRepository.findDistinctStudentNames();
        return names.toArray(new String[0]);
    }
}
