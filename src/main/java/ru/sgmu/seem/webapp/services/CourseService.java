package ru.sgmu.seem.webapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sgmu.seem.webapp.domains.Course;
import ru.sgmu.seem.webapp.repositories.CourseRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class CourseService implements CrudService<Course>{

    @Autowired
    CourseRepository courseRepository;

    @Override
    @Transactional
    public void add(Course course) {
        courseRepository.save(course);
    }

    @Override
    @Transactional
    public void update(Course course) {
        courseRepository.save(course);
    }

    @Override
    @Transactional
    public void remove(long id) {
        Course course = courseRepository.findOne(id);
        if (course != null) {
            courseRepository.delete(id);
        }
    }

    @Override
    public Course getById(long id) {
        return courseRepository.findOne(id);
    }

    @Override
    public List<Course> getAll() {
        return (List<Course>) courseRepository.findAll();
    }
}
