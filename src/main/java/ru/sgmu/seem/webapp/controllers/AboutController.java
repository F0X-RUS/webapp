package ru.sgmu.seem.webapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.sgmu.seem.webapp.domains.Passage;
import ru.sgmu.seem.webapp.services.PassageService;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static ru.sgmu.seem.utils.enums.ListTitle.PASSAGE_LIST;
import static ru.sgmu.seem.utils.enums.MenuOption.ABOUT;
import static ru.sgmu.seem.utils.enums.PageAttribute.CONTENT;
import static ru.sgmu.seem.utils.enums.PageAttribute.CURRENT_PAGE;
import static ru.sgmu.seem.utils.enums.PageAttribute.TITLE;

@Controller
@RequestMapping(value = "/about")
public class AboutController {

    private final static String ABOUT_TITLE = "О кафедре";

    private Path aboutFragmentPath = Paths.get("fragments", "about");

    private final PassageService passageService;

    @Autowired
    public AboutController(PassageService passageService){
        this.passageService = passageService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String about(Model model){
        List<Passage> list = passageService.getAll();
        model.addAttribute(CONTENT.name(), aboutFragmentPath)
                .addAttribute(TITLE.name(), ABOUT_TITLE)
                .addAttribute(CURRENT_PAGE.name(), ABOUT.name())
                .addAttribute(PASSAGE_LIST.name(),list);
        return "layouts/main";
    }
}
