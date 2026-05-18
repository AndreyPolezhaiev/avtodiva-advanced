package com.polezhaiev.avtodiva.repository;

import com.polezhaiev.avtodiva.model.template.time.ScheduleTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleTemplateRepository extends JpaRepository<ScheduleTemplate, Long> {
}
