package ru.sgmu.seem.webapp.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.sgmu.seem.webapp.domains.ContactType;

public interface ContactTypeDAO extends CrudRepository<ContactType, Long>{
}
