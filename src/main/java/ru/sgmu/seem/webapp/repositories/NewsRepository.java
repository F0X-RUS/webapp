package ru.sgmu.seem.webapp.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.sgmu.seem.webapp.domains.News;

import java.util.List;

public interface NewsRepository extends PagingAndSortingRepository<News, Long> {

}
