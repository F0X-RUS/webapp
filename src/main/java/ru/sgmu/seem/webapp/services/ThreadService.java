package ru.sgmu.seem.webapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Service;
import ru.sgmu.seem.webapp.domains.Discipline;
import ru.sgmu.seem.webapp.domains.Thread;
import ru.sgmu.seem.webapp.repositories.ThreadDAO;

import java.util.Collections;
import java.util.List;

@Service
public class ThreadService implements PageableService<Thread>, ThreadFindByService {

    private ThreadDAO threadDAO;

    @Autowired
    public ThreadService(ThreadDAO threadDAO) {
        this.threadDAO = threadDAO;
    }

    @Override
    public void add(Thread entity) {
        threadDAO.save(entity);
    }

    @Override
    public void update(Thread entity) {
        threadDAO.save(entity);
    }

    @Override
    public void remove(Long id) {
        if (threadDAO.exists(id)) {
            threadDAO.delete(id);
        }
    }

    @Override
    public Thread getById(Long id) {
        return threadDAO.findOne(id);
    }

    @Override
    public List<Thread> getAll() {
        return (List<Thread>) threadDAO.findAll();
    }

    @Override
    public List<Thread> getLast3() {
        List<Thread> threads = (List<Thread>) threadDAO.findAll();
        Collections.reverse(threads);
        if (threads.size() >= 3) {
            threads = threads.subList(0, 3);
        }
        return threads;
    }

    @Override
    public Page<Thread> findAll(Pageable pageable) {
        return threadDAO.findAll(pageable);
    }

    @Override
    public Page<Thread> findByDiscipline(long disc_id, Pageable pageable) {
        return threadDAO.findByDiscipline(disc_id, pageable);
    }

    @Override
    public Page<Thread> getThreadPage(long disc_id, int pageNumber, int size, Sort.Direction direction, String... orderParam) {
        PageRequest pageRequest = new PageRequest(pageNumber, size, direction, orderParam);
        return threadDAO.findByDiscipline(disc_id, pageRequest);
    }

    @Override
    public Page<Thread> getPage(int pageNumber, int size, Sort.Direction direction, String... orderParam) {
        PageRequest pageRequest = new PageRequest(pageNumber, size, direction, orderParam);
        return threadDAO.findAll(pageRequest);
    }
}
