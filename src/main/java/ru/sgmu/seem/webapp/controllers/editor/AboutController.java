package ru.sgmu.seem.webapp.controllers.editor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.sgmu.seem.utils.DateManager;
import ru.sgmu.seem.utils.enums.ListTitle;
import ru.sgmu.seem.utils.enums.MenuOption;
import ru.sgmu.seem.utils.enums.PageTitle;
import ru.sgmu.seem.webapp.domains.Passage;
import ru.sgmu.seem.webapp.services.PassageService;

import javax.validation.Valid;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import static ru.sgmu.seem.utils.enums.MenuOption.ABOUT;
import static ru.sgmu.seem.utils.enums.PageAttribute.CONTENT;
import static ru.sgmu.seem.utils.enums.PageAttribute.MENU_OPTION;
import static ru.sgmu.seem.utils.enums.PageAttribute.TITLE;
import static ru.sgmu.seem.utils.enums.PageTitle.ADD_CONTACTS;
import static ru.sgmu.seem.utils.enums.PageTitle.ADD_PASSAGE;
import static ru.sgmu.seem.utils.enums.PageTitle.CONTACTS;

@Controller("EditorAboutController")
@RequestMapping(value = "/editor/about")
public class AboutController {

    private Path aboutFragmentPath = Paths.get("editor", "fragments", "about", "about");
    private Path addPassageFragmentPath = Paths.get("editor", "fragments", "about", "add");

    private final PassageService passageService;

    public AboutController(PassageService passageService){
        this.passageService = passageService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String about(Model model){
        List<Passage> list = passageService.getAll();
        model.addAttribute(CONTENT.name(), aboutFragmentPath)
                .addAttribute(MENU_OPTION.name(), ABOUT.name())
                .addAttribute(TITLE.name(), CONTACTS.getText())
                .addAttribute(ListTitle.PASSAGE_LIST.name(), list);
        return "editor/layouts/index";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(Model model,
                      Passage Passage) {
        model.addAttribute(CONTENT.name(), addPassageFragmentPath)
                .addAttribute(MENU_OPTION.name(), MenuOption.CONTACTS.name())
                .addAttribute(TITLE.name(), ADD_PASSAGE.getText());
        return "editor/layouts/index";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String add(Model model,
                      @Valid Passage passage,
                      BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            model.addAttribute(CONTENT.name(), addPassageFragmentPath)
                    .addAttribute(MENU_OPTION.name(), MenuOption.ABOUT.name())
                    .addAttribute(TITLE.name(), ADD_PASSAGE.getText());
            return "editor/layouts/index";
        }
        //passage.setUpdatedBy();
        passageService.add(passage);
        List<Passage> list = passageService.getAll();
        list.sort((Passage e1, Passage e2) -> e1.getId().compareTo(e2.getId()) * -1);
        model.addAttribute(CONTENT.name(), aboutFragmentPath)
                .addAttribute(MENU_OPTION.name(), MenuOption.ABOUT.name())
                .addAttribute(TITLE.name(), PageTitle.ABOUT.getText())
                .addAttribute(ListTitle.PASSAGE_LIST.name(), list);
        return "editor/layouts/index";
    }
}
