package ru.sgmu.seem.webapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sgmu.seem.webapp.domains.Contact;
import ru.sgmu.seem.webapp.repositories.ContactDAO;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ContactService implements CrudService<Contact>{

    private final ContactDAO contactDAO;

    @Autowired
    public ContactService(ContactDAO contactDAO){
        this.contactDAO = contactDAO;
    }

    @Override
    public void add(Contact entity) {
        contactDAO.save(entity);
    }

    @Override
    public void update(Contact entity) {
        contactDAO.save(entity);
    }

    @Override
    public void remove(Long id) {
        contactDAO.delete(id);
    }

    @Override
    public Contact getById(Long id) {
        return contactDAO.findOne(id);
    }

    @Override
    public List<Contact> getAll() {
        return (List<Contact>) contactDAO.findAll();
    }

}
