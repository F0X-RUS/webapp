package ru.sgmu.seem.webapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.view.RedirectView;
import ru.sgmu.seem.webapp.domains.Course;
import ru.sgmu.seem.webapp.repositories.CourseRepository;
import ru.sgmu.seem.webapp.services.CourseService;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Controller
@RequestMapping(value = "/forum")
public class ForumController {

    @Autowired
    CourseService courseService;

    @RequestMapping(method = RequestMethod.GET)
    public String forumMain(Model model){
        List<Course> courseList = courseService.getAll();
        courseList.sort(Comparator.comparingLong(Course::getId));
        model.addAttribute("course_list", courseList);
        return "forum";
    }
}
