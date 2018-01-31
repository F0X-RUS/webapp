package ru.sgmu.seem.webapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.sgmu.seem.webapp.domains.Man;
import ru.sgmu.seem.webapp.repositories.ManRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ManService implements CrudService<Man> {

    @Autowired
    private ManRepository manRepository;

    @Override
    @Transactional
    public void add(Man man) {
        manRepository.save(man);
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional
    public void update(Man man) {
        manRepository.save(man);
    }

    @Override
    @Transactional
    public void remove(long id) {
        Man man = manRepository.findOne(id);
        if (man != null) {
            manRepository.delete(id);
        }
    }

    @Override
    public Man getById(long id) {
        return manRepository.findOne(id);
    }

    @Override
    public List<Man> getAll() {
        return (List<Man>) manRepository.findAll();
    }

}
