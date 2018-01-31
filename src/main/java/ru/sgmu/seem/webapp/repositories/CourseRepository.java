package ru.sgmu.seem.webapp.repositories;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.sgmu.seem.webapp.domains.Course;

import org.springframework.data.repository.CrudRepository;

@Repository
public interface CourseRepository extends CrudRepository<Course, Long> {

}
