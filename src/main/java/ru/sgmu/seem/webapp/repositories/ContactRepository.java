package ru.sgmu.seem.webapp.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.sgmu.seem.webapp.domains.Contact;

public interface ContactRepository extends CrudRepository<Contact, Long>{
}
