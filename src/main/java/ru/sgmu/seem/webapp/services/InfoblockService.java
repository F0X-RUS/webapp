package ru.sgmu.seem.webapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sgmu.seem.webapp.domains.Infoblock;
import ru.sgmu.seem.webapp.repositories.InfoblockRepository;

import javax.transaction.Transactional;
import java.util.List;


@Service
@Transactional
public class InfoblockService implements CrudService<Infoblock>{

    @Autowired
    InfoblockRepository infoblockRepository;

    @Override
    @Transactional
    public void add(Infoblock infoblock) {
        infoblockRepository.save(infoblock);
    }

    @Override
    @Transactional
    public void update(Infoblock infoblock) {
        infoblockRepository.save(infoblock);
    }

    @Override
    @Transactional
    public void remove(long id) {
        Infoblock infoblock = infoblockRepository.findOne(id);
        if (infoblock != null) {
            infoblockRepository.delete(id);
        }
    }

    @Override
    public Infoblock getById(long id) {
        return infoblockRepository.findOne(id);
    }

    @Override
    public List<Infoblock> getAll() {
        return (List<Infoblock>) infoblockRepository.findAll();
    }
}
