package ru.sgmu.seem.webapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.sgmu.seem.webapp.domains.Discipline;
import ru.sgmu.seem.webapp.domains.Thread;
import ru.sgmu.seem.webapp.repositories.DisciplineDAO;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class DisciplineService implements CrudService<Discipline>{

    private DisciplineDAO disciplineDAO;

    @Autowired
    public DisciplineService(DisciplineDAO disciplineDAO){
        this.disciplineDAO = disciplineDAO;
    }

    @Override
    @Transactional
    public void add(Discipline entity) {
        disciplineDAO.save(entity);
    }

    @Override
    @Transactional
    public void update(Discipline entity) {
        disciplineDAO.save(entity);
    }

    @Override
    @Transactional
    public void remove(Long id) {
        if(disciplineDAO.exists(id)) {
            disciplineDAO.delete(id);
        }
    }

    @Override
    public Discipline getById(Long id) {
        return disciplineDAO.findOne(id);
    }

    @Override
    public List<Discipline> getAll() {
        return (List<Discipline>)disciplineDAO.findAll();
    }

}
