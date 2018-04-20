package ru.sgmu.seem.webapp.domains.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import ru.sgmu.seem.webapp.domains.Man;
import ru.sgmu.seem.webapp.repositories.ManDAO;

@Component
//@CacheConfig(cacheNames = {"man"})
public class ManCache {

    private ManDAO manDAO;

    @Autowired
    public ManCache(ManDAO manDAO) {
        this.manDAO = manDAO;
    }

    @Cacheable(key="#id")
    public Man getOnCache(Long id){
        return manDAO.findOne(id);
    }
}
