package ru.sgmu.seem.webapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sgmu.seem.webapp.domains.News;
import ru.sgmu.seem.webapp.repositories.NewsRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class NewsServiceImpl implements NewsService {

    private NewsRepository newsRepository;

    @Autowired
    public void setNewsRepository(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    @Override
    @Transactional
    public void addNews(News news) {
        newsRepository.addNews(news);

    }

    @Override
    @Transactional
    public void updateNews(News news) {
        newsRepository.addNews(news);
    }

    @Override
    @Transactional
    public void removeNews(long id) {
        newsRepository.removeNews(id);
    }

    @Override
    @Transactional
    public News getNewsByID(long id) {
        return newsRepository.getNewsById(id);
    }

    @Override
    @Transactional
    public List<News> getAll() {
        List<News> newsList = new ArrayList<>();
        newsRepository.getAll().forEach(newsList::add);
        return newsList;
    }

    @Override
    @Transactional
    public List<News> getTopThree() {
        return this.newsRepository.getTopThree();
    }
}
