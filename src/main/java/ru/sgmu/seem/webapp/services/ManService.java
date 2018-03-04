package ru.sgmu.seem.webapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sgmu.seem.webapp.domains.Man;
import ru.sgmu.seem.webapp.repositories.ManDAO;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ManService implements CrudService<Man> {

    private final ManDAO manDAO;

    @Autowired
    public ManService(ManDAO manDAO) {
        this.manDAO = manDAO;
    }

    @Override
    @Transactional
    public void add(Man man) {
        manDAO.save(man);
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional
    public void update(Man man) {
        manDAO.save(man);
    }

    @Override
    @Transactional
    public void remove(long id) {
        Man man = manDAO.findOne(id);
        if (man != null) {
            manDAO.delete(id);
        }
    }

    @Override
    public Man getById(long id) {
        return manDAO.findOne(id);
    }

    @Override
    public List<Man> getAll() {
        return (List<Man>) manDAO.findAll();
    }

}
