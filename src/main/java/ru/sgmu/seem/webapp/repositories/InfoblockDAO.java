package ru.sgmu.seem.webapp.repositories;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.sgmu.seem.webapp.domains.Infoblock;

import java.util.List;

@Repository
public interface InfoblockDAO extends CrudRepository<Infoblock, Long> {

}
