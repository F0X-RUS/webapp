package ru.sgmu.seem.webapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.sgmu.seem.webapp.domains.Line;
import ru.sgmu.seem.webapp.services.CourseService;
import ru.sgmu.seem.webapp.services.DisciplineService;

import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

import static ru.sgmu.seem.utils.enums.PageAttribute.*;
import static ru.sgmu.seem.utils.enums.MenuOption.*;

@Controller
@RequestMapping(value = "/forum")
public class ForumController {

    private ResourceBundle resources = ResourceBundle.getBundle("strings");
    private CourseService courseService;
    private DisciplineService disciplineService;

    private Path forumFragmentPath = Paths.get("fragments", "forum");

    private static final String FORUM_TITLE = "Кафедра СТ и НМ - Форум";

    @Autowired
    public void setServices(CourseService courseService, DisciplineService disciplineService) {
        this.courseService = courseService;
        this.disciplineService = disciplineService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String forumMain(Model model) throws UnsupportedEncodingException{

        List<Line> lineList = courseService.getAll();
        lineList.sort(Comparator.comparingLong(Line::getId));
        model.addAttribute("list", lineList);
        model.addAttribute(CONTENT.name(), forumFragmentPath)
                .addAttribute(TITLE.name(), FORUM_TITLE)
                .addAttribute(CURRENT_PAGE.name(), FORUM.name())
                .addAttribute("text", resources.getString("main_page_title"));
        return "layouts/main";
    }

    @RequestMapping(value = "/{course_id}", method = RequestMethod.GET)
    public String disciplines(Model model,
                              @PathVariable("course_id") Long course_id) {
        //List<Discipline> disciplineList = courseService.getById(course_id).getDisciplines();
        //model.addAttribute("discipline", "current");
        //model.addAttribute("list", disciplineList);
        return "forum";
    }

}
