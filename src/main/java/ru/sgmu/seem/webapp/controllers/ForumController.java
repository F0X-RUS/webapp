package ru.sgmu.seem.webapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.sgmu.seem.utils.enums.PageTitle;

import java.nio.file.Path;
import java.nio.file.Paths;

import static ru.sgmu.seem.utils.enums.PageAttribute.*;
import static ru.sgmu.seem.utils.enums.MenuOption.*;

@Controller
@RequestMapping(value = "/forum")
public class ForumController {

    private Path forum = Paths.get("fragments", "forum");

    @RequestMapping(method = RequestMethod.GET)
    public String forumMain(Model model) {
        model.addAttribute(CONTENT.name(), forum)
                .addAttribute(TITLE.name(), PageTitle.FORUM)
                .addAttribute(CURRENT_PAGE.name(), FORUM.name());
        return "layouts/main";
    }
}
