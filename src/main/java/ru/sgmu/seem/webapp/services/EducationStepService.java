package ru.sgmu.seem.webapp.services;

import org.springframework.stereotype.Service;
import ru.sgmu.seem.webapp.domains.EducationStep;
import ru.sgmu.seem.webapp.repositories.EducationStepDAO;

import java.util.List;

@Service
public class EducationStepService implements CrudService<EducationStep>{

    private EducationStepDAO educationStepDAO;

    public EducationStepService(EducationStepDAO educationStepDAO){
        this.educationStepDAO = educationStepDAO;
    }

    @Override
    public void add(EducationStep entity) {

    }

    @Override
    public void update(EducationStep entity) {

    }

    @Override
    public void remove(long id) {

    }

    @Override
    public EducationStep getById(long id) {
        return null;
    }

    @Override
    public List<EducationStep> getAll() {
        return null;
    }

}
