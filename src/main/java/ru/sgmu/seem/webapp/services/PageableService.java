package ru.sgmu.seem.webapp.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.sgmu.seem.webapp.domains.News;

import java.util.List;

public interface PageableService<T> extends CrudService<T>{

    List<T> getLast3();

    Page<T> findAll(Pageable pageable);

    Page<T> getPage(int pageNumber, int size, Sort.Direction direction, String... orderParam);

}
