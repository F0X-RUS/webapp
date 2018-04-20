package ru.sgmu.seem.webapp.domains.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import ru.sgmu.seem.webapp.domains.Passage;
import ru.sgmu.seem.webapp.domains.Staff;
import ru.sgmu.seem.webapp.repositories.StaffDAO;

@Component
//@CacheConfig(cacheNames = {"staff"})
public class StaffCache {

    private StaffDAO staffDAO;

    @Autowired
    public StaffCache(StaffDAO staffDAO) {
        this.staffDAO = staffDAO;
    }

    @Cacheable(key="#id")
    public Staff getOnCache(Long id){
        return staffDAO.findOne(id);
    }
}
