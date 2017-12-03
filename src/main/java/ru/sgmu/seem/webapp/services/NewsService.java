package ru.sgmu.seem.webapp.services;

import ru.sgmu.seem.webapp.domains.News;

import java.util.List;

public interface NewsService {

    void addNews(News news);

    void updateNews(News news);

    void removeNews(long id);

    News getNewsByID(long id);

    List<News> getAll();

    List<News> getTopThree();
}
