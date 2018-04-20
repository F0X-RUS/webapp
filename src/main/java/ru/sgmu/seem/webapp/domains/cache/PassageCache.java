package ru.sgmu.seem.webapp.domains.cache;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import ru.sgmu.seem.webapp.domains.Passage;
import ru.sgmu.seem.webapp.repositories.PassageDAO;

@Component
//@CacheConfig(cacheNames = {"passage"})
public class PassageCache {

    private PassageDAO passageDAO;

    @Autowired
    public PassageCache(PassageDAO passageDAO) {
        this.passageDAO = passageDAO;
    }

    @Cacheable(key="#id")
    public Passage getOnCache(Long id){
        return passageDAO.findOne(id);
    }

}
