package ru.sgmu.seem.webapp.controllers.editor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.sgmu.seem.utils.enums.MenuOption;

import java.nio.file.Path;
import java.nio.file.Paths;

import static ru.sgmu.seem.utils.enums.PageAttribute.*;
import static ru.sgmu.seem.utils.enums.PageTitle.*;


@Controller("EditorHomeController")
@RequestMapping(value = "/editor")
public class HomeController {

    private Path viewFragmentPath = Paths.get("editor", "fragments", "view");

    @RequestMapping(method = RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute(CONTENT.name(), viewFragmentPath)
                .addAttribute(MENU_OPTION.name(), MenuOption.VIEW.name())
                .addAttribute(TITLE.name(), VIEW.getText());
        return "editor/layouts/index";
    }
}
