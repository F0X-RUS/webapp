package ru.sgmu.seem.webapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sgmu.seem.webapp.domains.File;
import ru.sgmu.seem.webapp.repositories.FileDAO;

import java.util.List;
import java.util.UUID;

@Service
public class FileService implements CrudService<File>{

    private FileDAO fileDAO;

    @Autowired
    public FileService(FileDAO fileDAO){
        this.fileDAO = fileDAO;
    }

    public void add(File entity) {
        fileDAO.save(entity);
    }

    public void update(File entity) {
        fileDAO.save(entity);
    }

    public void remove(Long id) {
        if (fileDAO.exists(id)) {
            fileDAO.delete(id);
        }
    }

    public File getById(Long id) {
        return fileDAO.findOne(id);
    }

    public List<File> getAll() {
        return (List<File>)fileDAO.findAll();
    }
}
