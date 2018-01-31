package ru.sgmu.seem.webapp.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.sgmu.seem.webapp.domains.News;

import java.util.List;

public interface NewsService extends CrudService<News> {

    List<News> getLast3();

    Page<News> findAll(Pageable pageable);

    Page<News> getPage(int pageNumber, int size, Sort.Direction direction, String... orderParam);

}
