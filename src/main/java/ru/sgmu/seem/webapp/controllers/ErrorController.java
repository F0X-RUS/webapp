package ru.sgmu.seem.webapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.nio.file.Path;
import java.nio.file.Paths;

import static ru.sgmu.seem.utils.enums.MenuOption.FORUM;
import static ru.sgmu.seem.utils.enums.PageAttribute.CONTENT;
import static ru.sgmu.seem.utils.enums.PageAttribute.MENU_OPTION;

@Controller
public class ErrorController {

    private Path deniedFragmentPath = Paths.get("denied");
    private Path errorFragmentPath = Paths.get("error");
    private Path uploadFragmentPath = Paths.get("upload-status");

    @RequestMapping(value="/wrongpage", method = RequestMethod.GET)
    public String accessDenied (Model model) {
        model.addAttribute(CONTENT.name(), deniedFragmentPath)
                .addAttribute(MENU_OPTION.name(), FORUM.name());
        return "layouts/main";
    }

    @RequestMapping(value="/error", method = RequestMethod.GET)
    public String error404 (Model model) {
        model.addAttribute(CONTENT.name(), errorFragmentPath)
                .addAttribute(MENU_OPTION.name(), FORUM.name());
        return "login";
    }

    @RequestMapping(value = "/uploadStatus", method = RequestMethod.GET)
    public String uploadError (Model model) {
        model.addAttribute(CONTENT.name(), uploadFragmentPath)
                .addAttribute(MENU_OPTION.name(), FORUM.name());
        return "layouts/main";
    }
}
