package ru.sgmu.seem.webapp.domains.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import ru.sgmu.seem.webapp.domains.News;
import ru.sgmu.seem.webapp.repositories.NewsDAO;

@Component
//@CacheConfig(cacheNames = {"news"})
public class NewsCache {

    private NewsDAO newsDAO;

    @Autowired
    public NewsCache(NewsDAO newsDAO) {
        this.newsDAO = newsDAO;
    }

    @Cacheable(key="#id")
    public News getOnCache(Long id){
        return newsDAO.findOne(id);
    }
}
