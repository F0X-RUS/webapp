package ru.sgmu.seem.webapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sgmu.seem.webapp.domains.Line;
import ru.sgmu.seem.webapp.domains.Discipline;
import ru.sgmu.seem.webapp.repositories.CourseRepository;
import ru.sgmu.seem.webapp.repositories.DisciplineRepository;

import java.util.List;

@Service
public class DisciplineService implements CrudService<Discipline>{

    private DisciplineRepository disciplineRepository;
    private CourseRepository courseRepository;

    @Autowired
    public DisciplineService(CourseRepository courseRepository,
                                DisciplineRepository disciplineRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public void add(Discipline entity) {
        disciplineRepository.save(entity);
    }

    @Override
    public void update(Discipline entity) {
        disciplineRepository.save(entity);
    }

    @Override
    public void remove(long id) {
        Discipline discipline = disciplineRepository.findOne(id);
        if (discipline != null) {
            disciplineRepository.delete(id);
        }
    }

    @Override
    public Discipline getById(long id) {
        return disciplineRepository.findOne(id);
    }

    @Override
    public List<Discipline> getAll() {
        return (List<Discipline>) disciplineRepository.findAll();
    }

    public void setCourses(Discipline discipline, List<Line> lineList){
        discipline.setLines(lineList);
        disciplineRepository.save(discipline);
    }
}
