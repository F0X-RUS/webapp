package ru.sgmu.seem.webapp.repositories.schedule;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.sgmu.seem.webapp.domains.schedule.StudyGroup;

@Repository
public interface GroupDAO extends CrudRepository<StudyGroup,Long>{
}
