package ru.sgmu.seem.webapp.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.sgmu.seem.webapp.domains.Discipline;

public interface DisciplineRepository extends CrudRepository<Discipline, Long> {
}
