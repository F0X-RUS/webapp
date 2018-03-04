package ru.sgmu.seem.webapp.controllers.editor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;
import ru.sgmu.seem.utils.FolderManager;
import ru.sgmu.seem.utils.enums.ListTitle;
import ru.sgmu.seem.utils.enums.MenuOption;
import ru.sgmu.seem.utils.enums.PageTitle;
import ru.sgmu.seem.webapp.domains.Passage;
import ru.sgmu.seem.webapp.services.PassageService;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static ru.sgmu.seem.utils.DateManager.getCurrentDate;
import static ru.sgmu.seem.utils.DateManager.getCurrentTime;
import static ru.sgmu.seem.utils.enums.MenuOption.ABOUT;
import static ru.sgmu.seem.utils.enums.PageAttribute.*;
import static ru.sgmu.seem.utils.enums.PageAttribute.CURRENT_PAGE;
import static ru.sgmu.seem.utils.enums.PageTitle.ADD_PASSAGE;
import static ru.sgmu.seem.utils.enums.PageTitle.CONTACTS;

@Controller("EditorAboutController")
@RequestMapping(value = "/editor/about")
public class AboutController {

    private Path about = Paths.get("editor", "fragments", "about", "about");
    private Path addPassage = Paths.get("editor", "fragments", "about", "add");
    private Path editPassage = Paths.get("editor", "fragments", "about", "edit");

    private final PassageService passageService;

    public AboutController(PassageService passageService){
        this.passageService = passageService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String about(Model model){
        List<Passage> list = passageService.getAll();
        model.addAttribute(CONTENT.name(), about)
                .addAttribute(MENU_OPTION.name(), ABOUT.name())
                .addAttribute(TITLE.name(), CONTACTS.getText())
                .addAttribute(ListTitle.PASSAGE_LIST.name(), list);
        return "editor/layouts/index";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(Model model,
                      Passage Passage) {
        model.addAttribute(CONTENT.name(), addPassage)
                .addAttribute(MENU_OPTION.name(), MenuOption.CONTACTS.name())
                .addAttribute(TITLE.name(), ADD_PASSAGE.getText());
        return "editor/layouts/index";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String add(Model model,
                      @Valid Passage passage,
                      BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            model.addAttribute(CONTENT.name(), addPassage)
                    .addAttribute(MENU_OPTION.name(), MenuOption.ABOUT.name())
                    .addAttribute(TITLE.name(), ADD_PASSAGE.getText());
            return "editor/layouts/index";
        }
        //passage.setUpdatedBy();
        passageService.add(passage);
        List<Passage> list = passageService.getAll();
        list.sort((Passage e1, Passage e2) -> e1.getId().compareTo(e2.getId()) * -1);
        model.addAttribute(CONTENT.name(), about)
                .addAttribute(MENU_OPTION.name(), MenuOption.ABOUT.name())
                .addAttribute(TITLE.name(), PageTitle.ABOUT.getText())
                .addAttribute(ListTitle.PASSAGE_LIST.name(), list);
        return "editor/layouts/index";
    }

    @RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
    public String Edit(Model model,
                           @PathVariable("id") Long id,
                           Passage passage) {
        passage = passageService.getById(id);
        model.addAttribute(CONTENT.toString(), editPassage)
                .addAttribute(TITLE.toString(), PageTitle.EDIT_PASSAGE.getText())
                .addAttribute(CURRENT_PAGE.name(), MenuOption.ABOUT.toString())
                .addAttribute("passage", passage);
        return "editor/layouts/index";
    }

    @RequestMapping(value = "/edit", params = "submit", method = RequestMethod.POST)
    public String EditSubmit(@Valid Passage passage,
                                 BindingResult bindingResult,
                                 Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute(CONTENT.name(), editPassage)
                    .addAttribute(TITLE.name(), PageTitle.EDIT_PASSAGE.getText())
                    .addAttribute(CURRENT_PAGE.name(), MenuOption.ABOUT.toString())
                    .addAttribute("passage", passage);
            return "editor/layouts/index";
        }
//        passage.setUpdatedBy();
        passage.setDate(getCurrentDate());
        passage.setTime(getCurrentTime());
        passageService.update(passage);
        return "redirect:/editor/about";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public RedirectView Delete(@RequestParam("id") Long id) {
        passageService.remove(id);
        return new RedirectView("/editor/about", true);
    }
}
