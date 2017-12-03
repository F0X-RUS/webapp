package ru.sgmu.seem.webapp.repositories;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.sgmu.seem.webapp.domains.News;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class NewsRepositoryImpl implements NewsRepository {

    private SessionFactory sessionFactory;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void addNews(News news) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(news);
    }

    @Override
    public void updateNews(News news) {
        Session session = sessionFactory.getCurrentSession();
        session.update(news);
    }

    @Override
    public void removeNews(long id) {
        Session session = sessionFactory.getCurrentSession();
        News news = (News) session.load(News.class, id);
        if (news != null) {
            session.delete(news);
        }
    }

    @Override
    public News getNewsById(long id) {
        Session session = sessionFactory.getCurrentSession();
        News news = (News) session.load(News.class, id);
        return news;
    }

    @Override
    public List<News> getAll() {
        Session session = sessionFactory.getCurrentSession();
        List<News> newsList = (List<News>) session.createQuery("from News").list();
        return newsList;
    }

    @Override
    public List<News> getTopThree() {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from News order by date,time desc");
        List<News> newsList = (List<News>) query.setMaxResults(3).list();
        return newsList;
    }
}