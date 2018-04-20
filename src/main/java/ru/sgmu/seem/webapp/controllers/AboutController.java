package ru.sgmu.seem.webapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.sgmu.seem.webapp.domains.Passage;
import ru.sgmu.seem.webapp.services.PassageService;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static ru.sgmu.seem.utils.FolderManager.NEWS_IMAGES_URL;
import static ru.sgmu.seem.utils.FolderManager.PASSAGE_IMAGES_URL;
import static ru.sgmu.seem.utils.enums.FolderTitle.NEWS_IMAGES;
import static ru.sgmu.seem.utils.enums.FolderTitle.PASSAGE_IMAGES;
import static ru.sgmu.seem.utils.enums.ListTitle.PASSAGE_LIST;
import static ru.sgmu.seem.utils.enums.MenuOption.ABOUT;
import static ru.sgmu.seem.utils.enums.PageAttribute.CONTENT;
import static ru.sgmu.seem.utils.enums.PageAttribute.MENU_OPTION;
import static ru.sgmu.seem.utils.enums.PageAttribute.TITLE;

@Controller
@RequestMapping(value = "/about")
public class AboutController {

    private Path aboutFragmentPath = Paths.get("fragments", "about");

    private final PassageService passageService;

    @Autowired
    public AboutController(PassageService passageService){
        this.passageService = passageService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String about(Model model){
        List<Passage> list = passageService.getAll();
        if (!model.containsAttribute("passage")){
            if (list.get(0) != null) {
                model.addAttribute("passage", list.get(0));
            } else{
                model.addAttribute("passage", new Passage());
            }
        }
        model.addAttribute(CONTENT.name(), aboutFragmentPath)
                .addAttribute(MENU_OPTION.name(), ABOUT.name())
                .addAttribute(PASSAGE_LIST.name(),list)
                .addAttribute(PASSAGE_IMAGES.name(), PASSAGE_IMAGES_URL);
        return "layouts/main";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String item(RedirectAttributes redirectAttributes,
                       @PathVariable("id") Long id){
        redirectAttributes.addFlashAttribute("passage", passageService.getById(id));
        return "redirect:/about";
    }

}
