package ru.sgmu.seem.webapp.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.sgmu.seem.webapp.domains.Role;

public interface RoleDAO extends CrudRepository<Role,Long>{
}
