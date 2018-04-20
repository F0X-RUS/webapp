package ru.sgmu.seem.webapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.sgmu.seem.webapp.domains.News;
import ru.sgmu.seem.webapp.domains.cache.NewsCache;
import ru.sgmu.seem.webapp.repositories.NewsDAO;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class NewsServiceImpl implements PageableService<News> {

    private final NewsDAO newsDAO;
//    private NewsCache newsCache;

    @Autowired
    public NewsServiceImpl(NewsDAO newsDAO,
                           NewsCache newsCache) {
        this.newsDAO = newsDAO;
//        this.newsCache = newsCache;
    }

    @Override
    @Transactional
    public void add(News news) {
        newsDAO.save(news);
    }

    @Override
    @Transactional
    public void update(News news) {
        newsDAO.save(news);
    }

    @Override
    public void remove(Long id) {
        newsDAO.delete(id);
    }

    @Override
    public News getById(Long id) {
        return newsDAO.findOne(id);
    }

    @Override
    public List<News> getAll() {
        return (List<News>) newsDAO.findAll();
    }

    @Override
    public List<News> getLast3() {
        List<News> newsList = (List<News>) newsDAO.findAll();
        Collections.reverse(newsList);
        if (newsList.size() >= 3){
            newsList = newsList.subList(0, 3);
        }
        return newsList;
    }

    @Override
    public Page<News> findAll(Pageable pageable) {
        return newsDAO.findAll(pageable);
    }

    @Override
    public Page<News> getPage(int pageNumber, int size, Sort.Direction direction, String... orderParam) {
        PageRequest pageRequest = new PageRequest(pageNumber, size, direction, orderParam);
        return newsDAO.findAll(pageRequest);
    }


}
