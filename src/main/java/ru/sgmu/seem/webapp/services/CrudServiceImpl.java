package ru.sgmu.seem.webapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public class CrudServiceImpl<T> implements CrudService<T>{

    private CrudRepository<T,Long> dao;

    @Autowired
    public CrudServiceImpl(CrudRepository<T, Long> dao) {
        this.dao = dao;
    }

    @Override
    public void add(T entity) {
        dao.save(entity);
    }

    @Override
    public void update(T entity) {
        dao.save(entity);
    }

    @Override
    public void remove(Long id) {
        dao.delete(id);
    }

    @Override
    public T getById(Long id) {
        return dao.findOne(id);
    }

    @Override
    public List<T> getAll() {
        return (List<T>)dao.findAll();
    }
}
