package ru.sgmu.seem.webapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.sgmu.seem.webapp.domains.Infoblock;
import ru.sgmu.seem.webapp.domains.Man;
import ru.sgmu.seem.webapp.services.CrudService;
import ru.sgmu.seem.webapp.services.NewsService;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static ru.sgmu.seem.utils.DateManager.*;
import static ru.sgmu.seem.utils.FolderManager.*;
import static ru.sgmu.seem.utils.enums.FolderTitle.*;
import static ru.sgmu.seem.utils.ImageManager.DEFAULT_IMAGE;
import static ru.sgmu.seem.utils.enums.ListTitle.*;
import static ru.sgmu.seem.utils.enums.MenuOption.*;
import static ru.sgmu.seem.utils.enums.PageAttribute.*;

@Controller
@RequestMapping(value = "")
public class HomeController {

    private final static String MAINPAGE_TITLE = "Кафедра симуляционного образования и неотложной медицины";
    private final NewsService newsService;
    private final CrudService<Man> manService;
    private final CrudService<Infoblock> infoblockService;

    private Path mainFragmentPath = Paths.get("fragments", "main");

    @Autowired
    public HomeController(NewsService newsService,
                          CrudService<Man> manService,
                          CrudService<Infoblock> infoblockService) {
        this.newsService = newsService;
        this.manService = manService;
        this.infoblockService = infoblockService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String main(Model model) {

        //TODO: MOVE TO ANOTHER CLASS
        if (manService.getAll().size() < 3) {
            initMainMan();
        }
        if (infoblockService.getAll().size() < 3) {
            initMainInfoblock();
        }

        List<Man> manList = manService.getAll();
        List<Infoblock> infoblockList = infoblockService.getAll();
        manList.sort((e1, e2) -> (Long.compare(e1.getId(), e2.getId())));
        infoblockList.sort((e1, e2) -> (Long.compare(e1.getId(), e2.getId())));
        model.addAttribute(MAN_LIST.name(), manList)
                .addAttribute(INFOBLOCK_LIST.name(), infoblockList)
                .addAttribute(NEWS_LIST.name(), newsService.getLast3())
                .addAttribute(CONTENT.name(), mainFragmentPath)
                .addAttribute(TITLE.name(), MAINPAGE_TITLE)
                .addAttribute(CURRENT_PAGE.name(), MAIN.name())
                .addAttribute(NEWS_IMAGES.getText(), NEWS_IMAGES_URL)
                .addAttribute(MAN_IMAGES.getText(), MAN_IMAGES_URL)
                .addAttribute(INFOBLOCK_IMAGES.getText(), INFOBLOCK_IMAGES_URL);
        return "layouts/main";
    }


    private void initMainMan() {
        Man man;
        for (int i = 0; i < 3; i++) {
            man = new Man();
            man.setImageName(DEFAULT_IMAGE);
            man.setName("Имя " + i);
            man.setSurname("Фамилия " + i);
            man.setPatronymic("Отчество " + i);
            man.setDescription("Описание " + i);
            man.setUpdatedBy("admin" + i);
            man.setDate(getCurrentDate());
            man.setTime(getCurrentTime());
            manService.add(man);
        }
    }

    private void initMainInfoblock() {
        Infoblock infoblock;
        for (int i = 0; i < 3; i++) {
            infoblock = new Infoblock();
            infoblock.setImageName(DEFAULT_IMAGE);
            infoblock.setTitle("Заголовок " + i);
            infoblock.setSlogan("Слоган " + i);
            infoblock.setDescription("Описание " + i);
            infoblock.setUpdatedBy("admin" + i);
            infoblock.setDate(getCurrentDate());
            infoblock.setTime(getCurrentTime());
            infoblockService.add(infoblock);
        }
    }
}
