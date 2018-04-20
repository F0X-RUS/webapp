package ru.sgmu.seem.webapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.sgmu.seem.webapp.configs.EncoderConfig;
import ru.sgmu.seem.webapp.domains.CustomUserDetails;
import ru.sgmu.seem.webapp.domains.Role;
import ru.sgmu.seem.webapp.domains.User;
import ru.sgmu.seem.webapp.repositories.RoleDAO;
import ru.sgmu.seem.webapp.repositories.UserDAO;

import java.util.*;

@Service
public class CustomUserDetailsService implements UserDetailsService, CrudService<User> {

    private UserDAO userDAO;
    private RoleService roleService;
    private EncoderConfig encoderConfig;

    @Autowired
    public CustomUserDetailsService(UserDAO userDAO,
                                    RoleService roleService,
                                    EncoderConfig encoderConfig) {
        this.userDAO = userDAO;
        this.roleService = roleService;
        this.encoderConfig = encoderConfig;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUsers = userDAO.findByUsername(username);
        optionalUsers.orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        return optionalUsers.map(CustomUserDetails::new).get();
    }

    @Override
    public void add(User entity) {
        entity.setEnable(true);
        Role user = roleService.getAll().stream()
                .filter(e -> e.getRole().equals("USER"))
                .findFirst().orElse(null);
        entity.setRole(user);
        entity.setPassword(encoderConfig.passwordEncoder().encode(entity.getPassword()));
        userDAO.save(entity);
    }

    @Override
    public void update(User entity) {
        userDAO.save(entity);
    }

    @Override
    public void remove(Long id) {
        userDAO.delete(id);
    }

    @Override
    public User getById(Long id) {
        return userDAO.findOne(id);
    }

    @Override
    public List<User> getAll() {
        return (List<User>) userDAO.findAll();
    }

    public User getByUsername(String username){
        User defUser = new User();
        defUser.setName("Unknown");
        defUser.setSurname("Unknown");
        return getAll().stream()
                .filter(e -> e.getUsername().equals(username))
                .findFirst().orElse(defUser);
    }
}
