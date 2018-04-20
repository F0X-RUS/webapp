package ru.sgmu.seem.webapp.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.sgmu.seem.webapp.domains.Post;

public interface PostDAO extends CrudRepository<Post,Long> {
}
