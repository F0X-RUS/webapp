package ru.sgmu.seem.webapp.services;

import java.util.List;

public interface CrudService<T> {

    void add(T entity);

    void update(T entity);

    void remove(long id);

    T getById(long id);

    List<T> getAll();
}
