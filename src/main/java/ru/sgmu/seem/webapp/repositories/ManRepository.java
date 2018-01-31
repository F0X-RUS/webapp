package ru.sgmu.seem.webapp.repositories;

import org.hibernate.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.sgmu.seem.webapp.domains.Man;

import java.util.List;

@Repository
public interface ManRepository extends CrudRepository<Man, Long> {

    /*@Autowired
    private SessionFactory sessionFactory;

    @Override
    public void add(Man entity) {
        Session session = this.sessionFactory.getCurrentSession();
        session.persist(entity);
    }

    @Override
    public void update(Man entity) {
        Session session = this.sessionFactory.getCurrentSession();
        session.update(entity);
    }

    @Override
    public void remove(long id) {
        Session session = this.sessionFactory.getCurrentSession();
        Man man = session.load(Man.class, id);
        if (man != null) {
            session.delete(man);
        }
    }

    @Override
    public Man getById(long id) {
        Session session = this.sessionFactory.getCurrentSession();
        Man man = session.load(Man.class, id);
        return man;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Man> getAll() {
        Session session = sessionFactory.getCurrentSession();
        List<Man> manList = (List<Man>) session.createQuery("from Man").list();
        return manList;
    }*/
}
