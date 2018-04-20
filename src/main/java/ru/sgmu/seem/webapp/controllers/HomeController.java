package ru.sgmu.seem.webapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.sgmu.seem.utils.InitManager;
import ru.sgmu.seem.webapp.domains.*;
import ru.sgmu.seem.webapp.services.CrudService;
import ru.sgmu.seem.webapp.services.CustomUserDetailsService;
import ru.sgmu.seem.webapp.services.PageableService;
import ru.sgmu.seem.webapp.services.RoleService;

import javax.persistence.Cacheable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static ru.sgmu.seem.utils.FolderManager.*;
import static ru.sgmu.seem.utils.enums.FolderTitle.*;
import static ru.sgmu.seem.utils.enums.ListTitle.*;
import static ru.sgmu.seem.utils.enums.MenuOption.*;
import static ru.sgmu.seem.utils.enums.PageAttribute.*;

@Controller
@RequestMapping(value = "")
@Cacheable
public class HomeController {
    private PageableService<News> newsPageableService;
    private InitManager initManager;
    private CrudService<Man> manService;
    private CrudService<Infoblock> infoblockService;

    private Path mainFragmentPath = Paths.get("fragments", "main");

    @Autowired
    public HomeController(PageableService<News> newsPageableService,
                          CrudService<Man> manService,
                          CrudService<Infoblock> infoblockService,
                          InitManager initManager) {
        this.newsPageableService = newsPageableService;
        this.initManager = initManager;
        this.manService = manService;
        this.infoblockService = infoblockService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String main(Model model) {
        initManager.initAll();
        List<Man> manList = manService.getAll();
        List<Infoblock> infoblockList = infoblockService.getAll();
        manList.sort((e1, e2) -> (Long.compare(e1.getId(), e2.getId())));
        infoblockList.sort((e1, e2) -> (Long.compare(e1.getId(), e2.getId())));
        model.addAttribute(MAN_LIST.name(), manList)
                .addAttribute(INFOBLOCK_LIST.name(), infoblockList)
                .addAttribute(NEWS_LIST.name(), newsPageableService.getLast3())
                .addAttribute(CONTENT.name(), mainFragmentPath)
                .addAttribute(MENU_OPTION.name(), MAIN.name())
                .addAttribute(NEWS_IMAGES.getText(), NEWS_IMAGES_URL)
                .addAttribute(MAN_IMAGES.getText(), MAN_IMAGES_URL)
                .addAttribute(INFOBLOCK_IMAGES.getText(), INFOBLOCK_IMAGES_URL);
        return "layouts/main";
    }


}
