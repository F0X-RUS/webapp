package ru.sgmu.seem.webapp.controllers.editor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.sgmu.seem.utils.enums.MenuOption;
import ru.sgmu.seem.webapp.domains.EducationStep;
import ru.sgmu.seem.webapp.domains.EntityDetails;
import ru.sgmu.seem.webapp.services.CrudService;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;

import static ru.sgmu.seem.utils.enums.ListTitle.EDUCATIONSTEPS_LIST;
import static ru.sgmu.seem.utils.enums.PageAttribute.CONTENT;
import static ru.sgmu.seem.utils.enums.PageAttribute.MENU_OPTION;
import static ru.sgmu.seem.utils.enums.PageAttribute.TITLE;
import static ru.sgmu.seem.utils.enums.PageTitle.FORUM;

@Controller("EditorForumController")
@RequestMapping(value = "/editor/forum")
public class ForumController {

    private Path forum = Paths.get("editor", "fragments", "forum", "forum");
    private CrudService<EducationStep> educationStepService;

    public ForumController(CrudService<EducationStep> educationStepService){
        this.educationStepService = educationStepService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String main(Model model){
        List<EducationStep> educationSteps = educationStepService.getAll();
        educationSteps.sort(Comparator.comparing(EntityDetails::getId));
        model.addAttribute(CONTENT.toString(), forum)
                .addAttribute(MENU_OPTION.toString(), MenuOption.FORUM.name())
                .addAttribute(TITLE.toString(), FORUM.getText())
                .addAttribute(EDUCATIONSTEPS_LIST.name(), educationSteps);
        return "editor/layouts/index";
    }
}
