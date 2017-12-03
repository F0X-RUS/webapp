package ru.sgmu.seem.webapp.repositories;

import org.springframework.context.annotation.Bean;
import org.springframework.data.repository.CrudRepository;
import ru.sgmu.seem.webapp.domains.News;

import java.util.List;

public interface NewsRepository {

    public void addNews(News news);

    public void updateNews(News news);

    public void removeNews(long id);

    public News getNewsById(long id);

    public List<News> getAll();

    public List<News> getTopThree();
}
