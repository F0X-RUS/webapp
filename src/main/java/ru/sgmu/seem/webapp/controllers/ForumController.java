package ru.sgmu.seem.webapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.sgmu.seem.utils.enums.PageTitle;
import ru.sgmu.seem.webapp.domains.*;
import ru.sgmu.seem.webapp.domains.Thread;
import ru.sgmu.seem.webapp.services.CrudService;
import ru.sgmu.seem.webapp.services.EducationStepService;

import javax.validation.Valid;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static ru.sgmu.seem.utils.DateManager.getCurrentDate;
import static ru.sgmu.seem.utils.DateManager.getCurrentTime;
import static ru.sgmu.seem.utils.enums.PageAttribute.*;
import static ru.sgmu.seem.utils.enums.MenuOption.*;

@Controller
@RequestMapping(value = "/forum")
public class ForumController {

    private Path forumLayout = Paths.get("fragments", "forum", "layouts", "forum");
    private Path forumMain = Paths.get("fragments", "forum", "forum");
    private Path coursesFragment = Paths.get("fragments", "forum", "courses");
    private Path threadFragment = Paths.get("fragments", "forum", "thread");
    private Path addThreadFragment = Paths.get("fragments", "forum", "add-thread");
    private CrudService<EducationStep> educationStepService;
    private CrudService<Course> courseCrudService;
    private CrudService<Specialization> specializationCrudService;
    private CrudService<Discipline> disciplineCrudService;
    private CrudService<Thread> threadCrudService;

    @Autowired
    public ForumController(CrudService<EducationStep> educationStepService,
                           CrudService<Course> courseCrudService,
                           CrudService<Specialization> specializationCrudService,
                           CrudService<Discipline> disciplineCrudService,
                           CrudService<Thread> threadCrudService) {
        this.educationStepService = educationStepService;
        this.courseCrudService = courseCrudService;
        this.specializationCrudService = specializationCrudService;
        this.disciplineCrudService = disciplineCrudService;
        this.threadCrudService = threadCrudService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String educationSteps(Model model) {
        List<EducationStep> educationSteps = educationStepService.getAll();
        educationSteps.sort(Comparator.comparing(EntityDetails::getId));
        model.addAttribute(CONTENT.name(), forumLayout)
                .addAttribute("FORUM_CONTENT", forumMain)
                .addAttribute(TITLE.name(), PageTitle.FORUM.getText())
                .addAttribute(CURRENT_PAGE.name(), FORUM.name())
                .addAttribute("list", educationSteps);
        return "layouts/main";
    }

    @RequestMapping(value = "/{educ_id}", method = RequestMethod.GET)
    public String courses(Model model,
                          @PathVariable("educ_id") Long educ_id) {
        SortedSet<Course> courses = new TreeSet<>(Comparator.comparing(EntityDetails::getId));
        courses.addAll(educationStepService.getById(educ_id).getCourses());
        model.addAttribute(CONTENT.name(), forumLayout)
                .addAttribute("FORUM_CONTENT", coursesFragment)
                .addAttribute(TITLE.name(), PageTitle.FORUM.getText())
                .addAttribute(CURRENT_PAGE.name(), FORUM.name())
                .addAttribute("list", courses)
                .addAttribute("current_id", educ_id)
                .addAttribute("educ", educationStepService.getById(educ_id));
        return "layouts/main";
    }


    //TODO: REMADE
    @RequestMapping(value = "/{educ_id}/{course_id}", method = RequestMethod.GET)
    public String specializations(Model model,
                                  @PathVariable("educ_id") Long educ_id,
                                  @PathVariable("course_id") Long course_id) {

        EducationStep educationStep = educationStepService.getById(educ_id);
        Course course = courseCrudService.getById(course_id);

        Set<Specialization> educSpecList = educationStep.getSpecializations();
        SortedSet<Specialization> courseSpecList = new TreeSet<>(Comparator.comparing(EntityDetails::getId));

        courseSpecList.addAll(course.getSpecializations());
        courseSpecList.retainAll(educSpecList);     // RETAIN SPECS FROM COURSE IN CURRENT EDUC_STEP


        model.addAttribute(CONTENT.name(), forumLayout)
                .addAttribute("FORUM_CONTENT", coursesFragment)
                .addAttribute(TITLE.name(), PageTitle.FORUM.getText())
                .addAttribute(CURRENT_PAGE.name(), FORUM.name())
                .addAttribute("list", courseSpecList)
                .addAttribute("current_id", course_id)
                .addAttribute("educ", educationStepService.getById(educ_id))
                .addAttribute("course", courseCrudService.getById(course_id));
        return "layouts/main";
    }

    @RequestMapping(value = "/{educ_id}/{course_id}/{spec_id}", method = RequestMethod.GET)
    public String disciplines(Model model,
                              @PathVariable("educ_id") Long educ_id,
                              @PathVariable("course_id") Long course_id,
                              @PathVariable("spec_id") Long spec_id) {
        EducationStep educationStep = educationStepService.getById(educ_id);
        Course course = courseCrudService.getById(course_id);
        Specialization specialization = specializationCrudService.getById(spec_id);
        Set<Discipline> educDiscList = educationStep.getDisciplines();
        Set<Discipline> courseDiscList = course.getDisciplines();
        Set<Discipline> specDiscList = specialization.getDisciplines();
        courseDiscList.retainAll(educDiscList);
        specDiscList.retainAll(courseDiscList);
        specDiscList = specDiscList.stream().sorted(Comparator.comparing(EntityDetails::getId)).collect(Collectors.toSet());
        model.addAttribute(CONTENT.name(), forumLayout)
                .addAttribute("FORUM_CONTENT", coursesFragment)
                .addAttribute(TITLE.name(), PageTitle.FORUM.getText())
                .addAttribute(CURRENT_PAGE.name(), FORUM.name())
                .addAttribute("list", specDiscList)
                .addAttribute("current_id", spec_id)
                .addAttribute("educ", educationStep)
                .addAttribute("course", course)
                .addAttribute("spec", specialization);
        return "layouts/main";
    }

    @RequestMapping(value = "/{educ_id}/{course_id}/{spec_id}/{disc_id}", method = RequestMethod.GET)
    public String threads(Model model,
                          @PathVariable("educ_id") Long educ_id,
                          @PathVariable("course_id") Long course_id,
                          @PathVariable("spec_id") Long spec_id,
                          @PathVariable("disc_id") Long disc_id) {
        EducationStep educationStep = educationStepService.getById(educ_id);
        Course course = courseCrudService.getById(course_id);
        Specialization specialization = specializationCrudService.getById(spec_id);
        Discipline discipline = disciplineCrudService.getById(disc_id);
        Set<Thread> educThreadList = educationStep.getThreads();
        Set<Thread> courseThreadList = course.getThreads();
        Set<Thread> specThreadList = specialization.getThreads();
        Set<Thread> discThreadList = discipline.getThreads();
        courseThreadList.retainAll(educThreadList);
        specThreadList.retainAll(courseThreadList);
        discThreadList.retainAll(specThreadList);
        discThreadList = discThreadList.stream().sorted(Comparator.comparing(EntityDetails::getId)).collect(Collectors.toSet());
        model.addAttribute(CONTENT.name(), forumLayout)
                .addAttribute("FORUM_CONTENT", coursesFragment)
                .addAttribute(TITLE.name(), PageTitle.FORUM.getText())
                .addAttribute(CURRENT_PAGE.name(), FORUM.name())
                .addAttribute("list", discThreadList)
                .addAttribute("current_id", disc_id)
                .addAttribute("educ", educationStep)
                .addAttribute("course", course)
                .addAttribute("spec", specialization)
                .addAttribute("disc", discipline);
        return "layouts/main";
    }

    @RequestMapping(value = "/{educ_id}/{course_id}/{spec_id}/{disc_id}/{thread_id}", method = RequestMethod.GET)
    public String thread(Model model,
                         @PathVariable("educ_id") Long educ_id,
                         @PathVariable("course_id") Long course_id,
                         @PathVariable("spec_id") Long spec_id,
                         @PathVariable("disc_id") Long disc_id,
                         @PathVariable("thread_id") Long thread_id) {
        Thread currentThread = threadCrudService.getById(thread_id);
        EducationStep educationStep = educationStepService.getById(educ_id);
        Course course = courseCrudService.getById(course_id);
        Specialization specialization = specializationCrudService.getById(spec_id);
        Discipline discipline = disciplineCrudService.getById(disc_id);
        model.addAttribute(CONTENT.name(), forumLayout)
                .addAttribute("FORUM_CONTENT", threadFragment)
                .addAttribute(TITLE.name(), PageTitle.FORUM.getText())
                .addAttribute(CURRENT_PAGE.name(), FORUM.name())
                .addAttribute("thread", currentThread)
                .addAttribute("educ", educationStep)
                .addAttribute("course", course)
                .addAttribute("spec", specialization)
                .addAttribute("disc", discipline);
        return "layouts/main";
    }

    @RequestMapping(value = "/{educ_id}/{course_id}/{spec_id}/{disc_id}/add", method = RequestMethod.GET)
    public String threadAdd(Model model,
                            @PathVariable("educ_id") Long educ_id,
                            @PathVariable("course_id") Long course_id,
                            @PathVariable("spec_id") Long spec_id,
                            @PathVariable("disc_id") Long disc_id,
                            Thread thread) {
        EducationStep educationStep = educationStepService.getById(educ_id);
        Course course = courseCrudService.getById(course_id);
        Specialization specialization = specializationCrudService.getById(spec_id);
        Discipline discipline = disciplineCrudService.getById(disc_id);
        model.addAttribute(CONTENT.name(), forumLayout)
                .addAttribute("FORUM_CONTENT", addThreadFragment)
                .addAttribute(TITLE.name(), PageTitle.FORUM.getText())
                .addAttribute(CURRENT_PAGE.name(), FORUM.name())
                .addAttribute("educ", educationStep)
                .addAttribute("course", course)
                .addAttribute("spec", specialization)
                .addAttribute("disc", discipline);
        return "layouts/main";
    }

    @RequestMapping(value = "/{educ_id}/{course_id}/{spec_id}/{disc_id}/add", method = RequestMethod.POST)
    public String threadSave(Model model,
                             @Valid Thread thread,
                             BindingResult bindingResult,
                             @PathVariable("educ_id") Long educ_id,
                             @PathVariable("course_id") Long course_id,
                             @PathVariable("spec_id") Long spec_id,
                             @PathVariable("disc_id") Long disc_id) {
        if (bindingResult.hasErrors()) {
            model.addAttribute(CONTENT.name(), forumLayout)
                    .addAttribute("FORUM_CONTENT", addThreadFragment)
                    .addAttribute(TITLE.name(), PageTitle.FORUM.getText())
                    .addAttribute(CURRENT_PAGE.name(), FORUM.name());
            return "layouts/main";
        }
        thread.setDate(getCurrentDate());
        thread.setTime(getCurrentTime());
        //TODO: SET UPDATED BY
//        thread.setUpdatedBy("");
        EducationStep educationStep = educationStepService.getById(educ_id);
        Course course = courseCrudService.getById(course_id);
        Specialization specialization = specializationCrudService.getById(spec_id);
        Discipline discipline = disciplineCrudService.getById(disc_id);
        thread.setDiscipline(discipline);
        thread.getEducationSteps().add(educationStep);
        thread.getCourses().add(course);
        thread.getSpecializations().add(specialization);
        threadCrudService.add(thread);
        educationStep.getThreads().add(thread);
        educationStepService.update(educationStep);
        course.getThreads().add(thread);
        courseCrudService.update(course);
        specialization.getThreads().add(thread);
        specializationCrudService.update(specialization);
        model.addAttribute(CONTENT.name(), forumLayout)
                .addAttribute("FORUM_CONTENT", threadFragment)
                .addAttribute(TITLE.name(), PageTitle.FORUM.getText())
                .addAttribute(CURRENT_PAGE.name(), FORUM.name());
        return "redirect:/forum/" + educ_id + "/" + course_id + "/" + spec_id + "/" + disc_id + "/" + thread.getId();
    }
}
