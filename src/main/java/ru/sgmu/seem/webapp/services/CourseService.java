package ru.sgmu.seem.webapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.sgmu.seem.webapp.domains.Line;
import ru.sgmu.seem.webapp.domains.Discipline;
import ru.sgmu.seem.webapp.repositories.CourseRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class CourseService implements CrudService<Line>{

    private final CourseRepository courseRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    @Transactional
    public void add(Line line) {
        courseRepository.save(line);
    }

    @Override
    @Transactional
    public void update(Line line) {
        courseRepository.save(line);
    }

    @Override
    @Transactional
    public void remove(long id) {
        Line line = courseRepository.findOne(id);
        if (line != null) {
            courseRepository.delete(id);
        }
    }

    @Override
    public Line getById(long id) {
        return courseRepository.findOne(id);
    }

    @Override
    public List<Line> getAll() {
        return (List<Line>) courseRepository.findAll();
    }

    public void setDisciplines(Line line, List<Discipline> disciplineList) {
        line.setDisciplines(disciplineList);
        courseRepository.save(line);
    }
}
