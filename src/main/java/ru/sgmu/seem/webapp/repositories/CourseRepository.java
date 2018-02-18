package ru.sgmu.seem.webapp.repositories;

import org.springframework.stereotype.Repository;
import ru.sgmu.seem.webapp.domains.Line;

import org.springframework.data.repository.CrudRepository;

@Repository
public interface CourseRepository extends CrudRepository<Line, Long> {
}
