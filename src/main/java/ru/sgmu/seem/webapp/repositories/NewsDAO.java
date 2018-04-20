package ru.sgmu.seem.webapp.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.sgmu.seem.webapp.domains.News;

import java.util.List;

@Repository
public interface NewsDAO extends PagingAndSortingRepository<News, Long> {

}
