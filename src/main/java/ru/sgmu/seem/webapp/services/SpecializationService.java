package ru.sgmu.seem.webapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sgmu.seem.webapp.domains.Specialization;
import ru.sgmu.seem.webapp.repositories.SpecializationDAO;

import java.util.List;

@Service
public class SpecializationService implements CrudService<Specialization> {

    private SpecializationDAO specializationDAO;

    @Autowired
    public SpecializationService(SpecializationDAO specializationDAO){
        this.specializationDAO = specializationDAO;
    }

    @Override
    public void add(Specialization entity) {
        specializationDAO.save(entity);
    }

    @Override
    public void update(Specialization entity) {
        specializationDAO.save(entity);
    }

    @Override
    public void remove(Long id) {
        if (specializationDAO.exists(id)){
            specializationDAO.delete(id);
        }
    }

    @Override
    public Specialization getById(Long id) {
        return specializationDAO.findOne(id);
    }

    @Override
    public List<Specialization> getAll() {
        return (List<Specialization>)specializationDAO.findAll();
    }
}
