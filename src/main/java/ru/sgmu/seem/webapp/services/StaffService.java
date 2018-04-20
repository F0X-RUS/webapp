package ru.sgmu.seem.webapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sgmu.seem.webapp.domains.Staff;
import ru.sgmu.seem.webapp.domains.cache.StaffCache;
import ru.sgmu.seem.webapp.repositories.StaffDAO;

import java.util.List;

@Service
public class StaffService implements CrudService<Staff>{

    private final StaffDAO staffDAO;
//    private StaffCache staffCache;

    @Autowired
    public StaffService(StaffDAO staffDAO,
                        StaffCache staffCache){
        this.staffDAO = staffDAO;
//        this.staffCache = staffCache;
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
    public void remove(Long id) {
        if (staffDAO.exists(id)){
            staffDAO.delete(id);
        }
    }

    @Override
    public Staff getById(Long id) {
        return staffDAO.findOne(id);
    }

    @Override
    public List<Staff> getAll() {
        return (List<Staff>) staffDAO.findAll();
    }
}
