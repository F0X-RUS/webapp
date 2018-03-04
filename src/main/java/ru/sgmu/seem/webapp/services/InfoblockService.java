package ru.sgmu.seem.webapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sgmu.seem.webapp.domains.Infoblock;
import ru.sgmu.seem.webapp.repositories.InfoblockDAO;

import javax.transaction.Transactional;
import java.util.List;


@Service
@Transactional
public class InfoblockService implements CrudService<Infoblock>{

    private final InfoblockDAO infoblockDAO;

    @Autowired
    public InfoblockService(InfoblockDAO infoblockDAO) {
        this.infoblockDAO = infoblockDAO;
    }

    @Override
    @Transactional
    public void add(Infoblock infoblock) {
        infoblockDAO.save(infoblock);
    }

    @Override
    @Transactional
    public void update(Infoblock infoblock) {
        infoblockDAO.save(infoblock);
    }

    @Override
    @Transactional
    public void remove(long id) {
        Infoblock infoblock = infoblockDAO.findOne(id);
        if (infoblock != null) {
            infoblockDAO.delete(id);
        }
    }

    @Override
    public Infoblock getById(long id) {
        return infoblockDAO.findOne(id);
    }

    @Override
    public List<Infoblock> getAll() {
        return (List<Infoblock>) infoblockDAO.findAll();
    }
}
