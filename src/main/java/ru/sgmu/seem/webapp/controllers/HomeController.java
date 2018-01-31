package ru.sgmu.seem.webapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.sgmu.seem.utils.DateManager;
import ru.sgmu.seem.utils.FolderManager;
import ru.sgmu.seem.webapp.domains.Infoblock;
import ru.sgmu.seem.webapp.domains.Man;
import ru.sgmu.seem.webapp.services.CrudService;
import ru.sgmu.seem.webapp.services.NewsService;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

import static ru.sgmu.seem.utils.ImageManager.DEFAULT_IMAGE;

@Controller
@RequestMapping(value = "")
public class HomeController {

    @Qualifier("newsServiceImpl")
    @Autowired
    private NewsService newsService;

    @Qualifier("manService")
    @Autowired
    private CrudService<Man> manService;

    @Qualifier("infoblockService")
    @Autowired
    private CrudService<Infoblock> infoblockService;

    @RequestMapping(method = RequestMethod.GET)
    public String getTopThree(Model model) {
        model.addAttribute("news_list", newsService.getLast3());
        model.addAttribute("topnews_upload_directory", FolderManager.NEWS_IMAGES_URL);
        model.addAttribute("man_upload_directory", FolderManager.MAN_IMAGES_URL);
        model.addAttribute("infoblock_upload_directory", FolderManager.INFOBLOCK_IMAGES_URL);
        if (manService.getAll().size() < 3){
            initMainMan();
        }
        if (infoblockService.getAll().size() < 3){
            initMainInfoblock();
        }
        List<Man> manList = manService.getAll();
        List<Infoblock> infoblockList = infoblockService.getAll();
        manList.sort((e1, e2) -> (Long.compare(e1.getId(), e2.getId())));
        infoblockList.sort((e1, e2) -> (Long.compare(e1.getId(), e2.getId())));
        model.addAttribute("man_list", manList);
        model.addAttribute("infoblock_list", infoblockList);
        return "main";
    }


    public void initMainMan(){
        Man man;
        for(int i = 0; i < 3; i++){
            man = new Man();
            man.setImageName(DEFAULT_IMAGE);
            man.setName("Имя " + i);
            man.setSurname("Фамилия " + i);
            man.setPatronymic("Отчество " + i);
            man.setDescription("Описание " + i);
            man.setUpdatedBy("admin" + i);
            man.setDate(Date.valueOf(DateManager.getCurrentDate()));
            man.setTime(Time.valueOf(DateManager.getCurrentTime()));
            manService.add(man);
        }
    }

    public void initMainInfoblock(){
        Infoblock infoblock;
        for(int i = 0; i < 3; i++){
            infoblock = new Infoblock();
            infoblock.setImageName(DEFAULT_IMAGE);
            infoblock.setTitle("Заголовок " + i);
            infoblock.setSlogan("Слоган " + i);
            infoblock.setDescription("Описание " + i);
            infoblock.setUpdatedBy("admin" + i);
            infoblock.setDate(Date.valueOf(DateManager.getCurrentDate()));
            infoblock.setTime(Time.valueOf(DateManager.getCurrentTime()));
            infoblockService.add(infoblock);
        }
    }
}
