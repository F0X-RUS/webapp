package ru.sgmu.seem.webapp.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.sgmu.seem.webapp.domains.Discipline;

@Repository
public interface DisciplineDAO extends CrudRepository<Discipline, Long> {
}
