package ru.sgmu.seem.webapp.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.sgmu.seem.webapp.domains.Course;

@Repository
public interface CourseDAO extends CrudRepository<Course, Long>{
}
