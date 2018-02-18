package ru.sgmu.seem.webapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sgmu.seem.webapp.domains.Contact;
import ru.sgmu.seem.webapp.repositories.ContactRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ContactService implements CrudService<Contact>{

    private final ContactRepository contactRepository;

    @Autowired
    public ContactService(ContactRepository contactRepository){
        this.contactRepository = contactRepository;
    }

    @Override
    public void add(Contact entity) {
        contactRepository.save(entity);
    }

    @Override
    public void update(Contact entity) {
        contactRepository.save(entity);
    }

    @Override
    public void remove(long id) {
        contactRepository.delete(id);
    }

    @Override
    public Contact getById(long id) {
        return contactRepository.findOne(id);
    }

    @Override
    public List<Contact> getAll() {
        return (List<Contact>) contactRepository.findAll();
    }
}
