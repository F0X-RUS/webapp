package ru.sgmu.seem.webapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sgmu.seem.webapp.domains.Staff;
import ru.sgmu.seem.webapp.repositories.StaffDAO;

import java.util.List;

@Service
public class StaffService implements CrudService<Staff>{

    private final StaffDAO staffDAO;

    @Autowired
    public StaffService(StaffDAO staffDAO){
        this.staffDAO = staffDAO;
    }

    @Override
    public void add(Staff entity) {
        staffDAO.save(entity);
    }

    @Override
    public void update(Staff entity) {
        staffDAO.save(entity);
    }

    @Override
    public void remove(long id) {
        if (staffDAO.exists(id)){
            staffDAO.delete(id);
        }
    }

    @Override
    public Staff getById(long id) {
        return staffDAO.findOne(id);
    }

    @Override
    public List<Staff> getAll() {
        return (List<Staff>) staffDAO.findAll();
    }
}
