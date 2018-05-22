package ru.sgmu.seem.webapp.services.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sgmu.seem.webapp.repositories.schedule.GroupDAO;
import ru.sgmu.seem.webapp.services.CrudService;
import ru.sgmu.seem.webapp.domains.schedule.StudyGroup;

import java.util.List;

@Service
public class GroupService implements CrudService<StudyGroup>{

    private GroupDAO groupDAO;

    @Autowired
    public GroupService(GroupDAO groupDAO) {
        this.groupDAO = groupDAO;
    }

    @Override
    public void add(StudyGroup entity) {
        groupDAO.save(entity);
    }

    @Override
    public void update(StudyGroup entity) {
        groupDAO.save(entity);
    }

    @Override
    public void remove(Long id) {
        groupDAO.delete(id);
    }

    @Override
    public StudyGroup getById(Long id) {
        return groupDAO.findOne(id);
    }

    @Override
    public List<StudyGroup> getAll() {
        return (List<StudyGroup>) groupDAO.findAll();
    }
}
