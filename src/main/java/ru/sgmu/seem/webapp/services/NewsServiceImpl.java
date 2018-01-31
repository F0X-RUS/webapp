package ru.sgmu.seem.webapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.sgmu.seem.webapp.domains.News;
import ru.sgmu.seem.webapp.repositories.NewsRepository;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class NewsServiceImpl implements NewsService {

    @Autowired
    private NewsRepository newsRepository;

    @Override
    @Transactional
    public void add(News news) {
        newsRepository.save(news);
    }

    @Override
    @Transactional
    public void update(News news) {
        newsRepository.save(news);
    }

    @Override
    public void remove(long id) {
        newsRepository.delete(id);
    }

    @Override
    public News getById(long id) {
        return newsRepository.findOne(id);
    }

    @Override
    public List<News> getAll() {
        return (List<News>) newsRepository.findAll();
    }

    @Override
    public List<News> getLast3() {
        List<News> newsList = (List<News>) newsRepository.findAll();
        Collections.reverse(newsList);
        return newsList.subList(0, 3);
    }

    @Override
    public Page<News> findAll(Pageable pageable) {
        return newsRepository.findAll(pageable);
    }

    @Override
    public Page<News> getPage(int pageNumber, int size, Sort.Direction direction, String... orderParam) {
        PageRequest pageRequest = new PageRequest(pageNumber, size, direction, orderParam);
        return newsRepository.findAll(pageRequest);
    }


}
