package ru.sgmu.seem.webapp.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.sgmu.seem.webapp.domains.Thread;

public interface ThreadDAO extends CrudRepository<Thread,Long>{
}
