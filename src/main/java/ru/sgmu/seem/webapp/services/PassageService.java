package ru.sgmu.seem.webapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sgmu.seem.webapp.domains.Passage;
import ru.sgmu.seem.webapp.repositories.PassageDAO;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class PassageService implements CrudService<Passage>{

    private final PassageDAO passageDAO;

    @Autowired
    public PassageService(PassageDAO passageDAO){
        this.passageDAO = passageDAO;
    }

    @Override
    public void add(Passage entity) {
        passageDAO.save(entity);
    }

    @Override
    public void update(Passage entity) {
        passageDAO.save(entity);
    }

    @Override
    public void remove(Long id) {
        Passage passage = passageDAO.findOne(id);
        if (passage != null){
            passageDAO.delete(id);
        }
    }

    @Override
    public Passage getById(Long id) {
        return passageDAO.findOne(id);
    }

    @Override
    public List<Passage> getAll() {
        return (List<Passage>) passageDAO.findAll();
    }
}
