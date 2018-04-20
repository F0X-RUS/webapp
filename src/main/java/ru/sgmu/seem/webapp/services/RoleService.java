package ru.sgmu.seem.webapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sgmu.seem.webapp.domains.Role;
import ru.sgmu.seem.webapp.repositories.RoleDAO;

import java.util.List;

@Service
public class RoleService implements CrudService<Role> {

    private RoleDAO roleDAO;

    @Autowired
    public RoleService(RoleDAO roleDAO) {
        this.roleDAO = roleDAO;
    }

    @Override
    public void add(Role entity) {
        roleDAO.save(entity);
    }

    @Override
    public void update(Role entity) {
        roleDAO.save(entity);
    }

    @Override
    public void remove(Long id) {
        roleDAO.delete(id);
    }

    @Override
    public Role getById(Long id) {
        return roleDAO.findOne(id);
    }

    @Override
    public List<Role> getAll() {
        return (List<Role>) roleDAO.findAll();
    }
}
