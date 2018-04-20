package ru.sgmu.seem.webapp.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.sgmu.seem.webapp.domains.Thread;

public interface ThreadFindByService {
    Page<Thread> findByDiscipline(long disc_id, Pageable pageable);
    Page<Thread> getThreadPage(long disc_id, int pageNumber, int size, Sort.Direction direction, String... orderParam);
}
