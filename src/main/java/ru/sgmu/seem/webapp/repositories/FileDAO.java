package ru.sgmu.seem.webapp.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.sgmu.seem.webapp.domains.File;

import java.util.UUID;

@Repository
public interface FileDAO extends CrudRepository<File, Long> {
}
