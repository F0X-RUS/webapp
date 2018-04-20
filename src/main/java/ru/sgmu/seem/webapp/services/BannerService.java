package ru.sgmu.seem.webapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import ru.sgmu.seem.webapp.domains.Banner;
import ru.sgmu.seem.webapp.repositories.BannerDAO;

import java.util.List;

public class BannerService implements CrudService<Banner>{

    private BannerDAO bannerDAO;

    @Autowired
    public BannerService(BannerDAO bannerDAO) {
        this.bannerDAO = bannerDAO;
    }

    @Override
    public void add(Banner entity) {
        bannerDAO.save(entity);
    }

    @Override
    public void update(Banner entity) {

    }

    @Override
    public void remove(Long id) {

    }

    @Override
    public Banner getById(Long id) {
        return null;
    }

    @Override
    public List<Banner> getAll() {
        return null;
    }
}
