package ru.sgmu.seem.webapp.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.file.Path;
import java.nio.file.Paths;

import static ru.sgmu.seem.utils.enums.MenuOption.FORUM;
import static ru.sgmu.seem.utils.enums.MenuOption.MAIN;
import static ru.sgmu.seem.utils.enums.PageAttribute.CONTENT;
import static ru.sgmu.seem.utils.enums.PageAttribute.MENU_OPTION;

@Controller
public class AuthController {

    private Path loginFragmentPath = Paths.get("login");

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginPage(Model model) {
        model.addAttribute(CONTENT.name(), loginFragmentPath)
                .addAttribute(MENU_OPTION.name(), FORUM.name());
        return "layouts/main";
    }

    @RequestMapping(value = "/login-error")
    public String loginError(Model model) {
        model.addAttribute(CONTENT.name(), loginFragmentPath)
                .addAttribute(MENU_OPTION.name(), FORUM.name())
                .addAttribute("loginError", true);
        return "login";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/";
    }
}
