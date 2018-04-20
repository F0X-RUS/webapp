package ru.sgmu.seem.webapp.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.sgmu.seem.webapp.domains.Thread;

@Repository
public interface ThreadDAO extends PagingAndSortingRepository<Thread, Long> {

    @Query(value = "SELECT thread.* FROM thread WHERE discipline_id = ?1 ORDER BY ?#{#pageable}",
            countQuery = "SELECT count(*) FROM thread WHERE discipline_id = ?1 ORDER BY ?#{#pageable}",
            nativeQuery = true)
    Page<Thread> findByDiscipline(long disc_id, Pageable pageable);
}
