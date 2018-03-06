package ru.sgmu.seem.webapp.services;

import org.springframework.stereotype.Service;
import ru.sgmu.seem.webapp.domains.Course;
import ru.sgmu.seem.webapp.repositories.CourseDAO;

import java.util.List;

@Service
public class CourseService implements CrudService<Course>{

    private CourseDAO courseDAO;

    public CourseService(CourseDAO courseDAO){
        this.courseDAO = courseDAO;
    }

    @Override
    public void add(Course entity) {
        courseDAO.save(entity);
    }

    @Override
    public void update(Course entity) {
        courseDAO.save(entity);
    }

    @Override
    public void remove(Long id) {
        if (courseDAO.exists(id)) {
            courseDAO.delete(id);
        }
    }

    @Override
    public Course getById(Long id) {
        return courseDAO.findOne(id);
    }

    @Override
    public List<Course> getAll() {
        return (List<Course>)courseDAO.findAll();
    }
}
