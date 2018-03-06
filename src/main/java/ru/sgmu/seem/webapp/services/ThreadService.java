package ru.sgmu.seem.webapp.services;

import org.springframework.stereotype.Service;
import ru.sgmu.seem.webapp.domains.Thread;
import ru.sgmu.seem.webapp.repositories.ThreadDAO;

import java.util.List;

@Service
public class ThreadService implements CrudService<Thread>{

    private ThreadDAO threadDAO;

    public ThreadService(ThreadDAO threadDAO){
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
        if(threadDAO.exists(id)){
            threadDAO.delete(id);
        }
    }

    @Override
    public Thread getById(Long id) {
        return threadDAO.findOne(id);
    }

    @Override
    public List<Thread> getAll() {
        return (List<Thread>)threadDAO.findAll();
    }
}
