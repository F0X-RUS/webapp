package ru.sgmu.seem.webapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import ru.sgmu.seem.webapp.domains.User;

import java.util.Optional;

public interface UserDAO extends CrudRepository<User,Long> {
    Optional<User> findByUsername(String username);
}
