package ru.sgmu.seem.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.sgmu.seem.webapp.domains.*;
import ru.sgmu.seem.webapp.services.CrudService;
import ru.sgmu.seem.webapp.services.CustomUserDetailsService;
import ru.sgmu.seem.webapp.services.RoleService;

import static ru.sgmu.seem.utils.DateManager.getCurrentDate;
import static ru.sgmu.seem.utils.DateManager.getCurrentTime;

@Component
public class InitManager {

    private CrudService<Man> manService;
    private CrudService<Infoblock> infoblockService;
    private CustomUserDetailsService userService;
    private RoleService roleService;

    @Autowired
    public InitManager(RoleService roleService,
                       CrudService<Man> manService,
                       CrudService<Infoblock> infoblockService,
                       CustomUserDetailsService userService) {
        this.roleService = roleService;
        this.manService = manService;
        this.infoblockService = infoblockService;
        this.userService = userService;
    }

    public void initAll() {
        if (userService.getAll().size() == 0) {
            initRolesAndAdmin();
        }
        if (manService.getAll().size() < 3) {
            initMan();
        }
        if (infoblockService.getAll().size() < 3) {
            initInfoblock();
        }
    }

    private void initMan() {
        for (int i = 0; i < 3; i++) {
            Man man = new Man("Имя", "Фамилия", "Отчество", "Описание");
            man.setUser(userService.getByUsername("system"));
            man.setDate(getCurrentDate());
            man.setTime(getCurrentTime());
            manService.add(man);
        }
    }

    private void initInfoblock() {
        for (int i = 0; i < 3; i++) {
            Infoblock infoblock = new Infoblock("Заголовок", "Слоган", "Описание");
            infoblock.setUser(userService.getByUsername("system"));
            infoblock.setDate(getCurrentDate());
            infoblock.setTime(getCurrentTime());
            infoblockService.add(infoblock);
        }
    }

    private void initRolesAndAdmin(){
        Role user = new Role("USER");
        Role admin = new Role("ADMIN");
        Role teacher = new Role("TEACHER");
        Role moder = new Role("MODER");
        roleService.add(user);
        roleService.add(admin);
        roleService.add(teacher);
        roleService.add(moder);
        User system = new User();
        system.setUsername("system");
        system.setPassword("1234");
        system.setName("System");
        system.setSurname("System");
        system.setPatronymic("System");
        userService.add(system);
        system.setRole(admin);
    }
}
