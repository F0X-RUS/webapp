package ru.sgmu.seem.webapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sgmu.seem.webapp.domains.ContactType;
import ru.sgmu.seem.webapp.repositories.ContactTypeDAO;

import java.util.List;

@Service
public class ContactTypeService  implements CrudService<ContactType>{

    private ContactTypeDAO contactTypeDAO;

    @Autowired
    public ContactTypeService(ContactTypeDAO contactTypeDAO) {
        this.contactTypeDAO = contactTypeDAO;
    }

    @Override
    public void add(ContactType entity) {
        contactTypeDAO.save(entity);
    }

    @Override
    public void update(ContactType entity) {
        contactTypeDAO.save(entity);
    }

    @Override
    public void remove(Long id) {
        contactTypeDAO.delete(id);
    }

    @Override
    public ContactType getById(Long id) {
        return contactTypeDAO.findOne(id);
    }

    @Override
    public List<ContactType> getAll() {
        return (List<ContactType>)contactTypeDAO.findAll();
    }
}
