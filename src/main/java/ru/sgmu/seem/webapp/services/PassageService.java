package ru.sgmu.seem.webapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sgmu.seem.webapp.domains.Passage;
import ru.sgmu.seem.webapp.repositories.PassageRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class PassageService implements CrudService<Passage>{

    private final PassageRepository passageRepository;

    @Autowired
    public PassageService(PassageRepository passageRepository){
        this.passageRepository = passageRepository;
    }

    @Override
    public void add(Passage entity) {
        passageRepository.save(entity);
    }

    @Override
    public void update(Passage entity) {
        passageRepository.save(entity);
    }

    @Override
    public void remove(long id) {
        Passage passage = passageRepository.findOne(id);
        if (passage == null){
            passageRepository.delete(id);
        }
    }

    @Override
    public Passage getById(long id) {
        return passageRepository.findOne(id);
    }

    @Override
    public List<Passage> getAll() {
        return (List<Passage>)passageRepository.findAll();
    }
}
