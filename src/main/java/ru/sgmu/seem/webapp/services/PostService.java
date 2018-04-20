package ru.sgmu.seem.webapp.services;

import org.springframework.stereotype.Service;
import ru.sgmu.seem.webapp.domains.Post;
import ru.sgmu.seem.webapp.repositories.PostDAO;

import java.util.List;

@Service
public class PostService implements CrudService<Post> {

    private PostDAO postDAO;

    public PostService(PostDAO postDAO) {
        this.postDAO = postDAO;
    }

    @Override
    public void add(Post entity) {
        postDAO.save(entity);
    }

    @Override
    public void update(Post entity) {
        postDAO.save(entity);
    }

    @Override
    public void remove(Long id) {
        postDAO.delete(id);
    }

    @Override
    public Post getById(Long id) {
        return postDAO.findOne(id);
    }

    @Override
    public List<Post> getAll() {
        return (List<Post>)postDAO.findAll();
    }
}
