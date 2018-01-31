package ru.sgmu.seem.webapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping(value = "/auth")
public class AuthController {

    @RequestMapping(method = RequestMethod.GET)
    public String auth(Model model){
        return "auth";
    }

    @RequestMapping(method = RequestMethod.POST)
    public RedirectView authPost(Model model){
        return new RedirectView("/forum");
    }
}
