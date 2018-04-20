package ru.sgmu.seem.webapp.services;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CrudService<T> {

    void add(T entity);

    void update(T entity);

    void remove(Long id);

    T getById(Long id);

    List<T> getAll();
}
