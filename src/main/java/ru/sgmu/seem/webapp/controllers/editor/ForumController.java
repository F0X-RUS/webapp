package ru.sgmu.seem.webapp.controllers.editor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import ru.sgmu.seem.utils.FileManager;
import ru.sgmu.seem.utils.PageWrapper;
import ru.sgmu.seem.utils.enums.MenuOption;
import ru.sgmu.seem.webapp.domains.*;
import ru.sgmu.seem.webapp.domains.Thread;
import ru.sgmu.seem.webapp.services.CrudService;
import ru.sgmu.seem.webapp.services.CustomUserDetailsService;
import ru.sgmu.seem.webapp.services.ThreadService;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.Comparator;
import java.util.List;

import static ru.sgmu.seem.utils.DateManager.getCurrentDate;
import static ru.sgmu.seem.utils.DateManager.getCurrentTime;
import static ru.sgmu.seem.utils.FolderManager.threadFilesPath;
import static ru.sgmu.seem.utils.enums.ListTitle.EDUCATIONSTEPS_LIST;
import static ru.sgmu.seem.utils.enums.PageAttribute.CONTENT;
import static ru.sgmu.seem.utils.enums.PageAttribute.MENU_OPTION;

@PreAuthorize("hasAnyRole('ADMIN','MODER')")
@Controller("EditorForumController")
@RequestMapping(value = "/editor/forum")
public class ForumController {

    private Path forum = Paths.get("editor", "fragments", "forum", "forum");
    private Path action = Paths.get("editor", "fragments", "forum", "action");
    private Path threads = Paths.get("editor", "fragments", "forum", "threads");
    private CrudService<EducationStep> educationStepService;
    private CrudService<Course> courseService;
    private CrudService<Specialization> specializationService;
    private CrudService<Discipline> disciplineService;
    private ThreadService threadService;
    private CustomUserDetailsService userService;
    private FileManager fileManager;
    private CrudService<File> fileService;

    @Autowired
    public ForumController(CrudService<EducationStep> educationStepService,
                           CrudService<Course> courseService,
                           CrudService<Specialization> specializationService,
                           CrudService<Discipline> disciplineService,
                           ThreadService threadService,
                           CustomUserDetailsService userService,
                           FileManager fileManager,
                           CrudService<File> fileService) {
        this.educationStepService = educationStepService;
        this.courseService = courseService;
        this.specializationService = specializationService;
        this.disciplineService = disciplineService;
        this.threadService = threadService;
        this.userService = userService;
        this.fileManager = fileManager;
        this.fileService = fileService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String main(Model model) {
        List<EducationStep> educationSteps = educationStepService.getAll();
        educationSteps.sort(Comparator.comparing(EntityDetails::getId));
        model.addAttribute(CONTENT.toString(), forum)
                .addAttribute(MENU_OPTION.toString(), MenuOption.FORUM.name())
                .addAttribute(EDUCATIONSTEPS_LIST.name(), educationSteps);
        return "editor/layouts/index";
    }

    /*
     *                   EDUCATION STEP
     */
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(Model model) {
        String title = "Добавить уровень образования";
        if (!model.containsAttribute("educationStep")) {
            model.addAttribute("educationStep", new EducationStep());
        }
        model.addAttribute(CONTENT.toString(), action)
                .addAttribute("PAGE_TITLE", title)
                .addAttribute(MENU_OPTION.toString(), MenuOption.FORUM.name())
                .addAttribute("action", "add");
        return "editor/layouts/index";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String saveEducStep(@Valid EducationStep educationStep,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes,
                               Principal principal) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.educationStep", bindingResult);
            redirectAttributes.addFlashAttribute("educationStep", educationStep);
            return "redirect:/editor/forum/add";
        }
        User user = userService.getByUsername(principal.getName());
        educationStep.setUser(user);
        educationStep.setTime(getCurrentTime());
        educationStep.setDate(getCurrentDate());
        educationStepService.add(educationStep);
        return "redirect:/editor/forum";
    }

    @RequestMapping(value = "/{educ_id}/edit", method = RequestMethod.GET)
    public String editEduc(Model model,
                           @PathVariable("educ_id") Long educ_id) {
        EducationStep educationStep = educationStepService.getById(educ_id);
        String title = "Изменить уровень образования - " + educationStep.getName();
        if (!model.containsAttribute("educationStep")) {
            model.addAttribute("educationStep", educationStep);
        }
        model.addAttribute(CONTENT.toString(), action)
                .addAttribute("PAGE_TITLE", title)
                .addAttribute(MENU_OPTION.toString(), MenuOption.FORUM.name())
                .addAttribute("action", "edit");
        return "editor/layouts/index";
    }

    @RequestMapping(value = "/{educ_id}/edit", method = RequestMethod.POST)
    public String saveEditEducStep(@Valid EducationStep educationStep,
                                   BindingResult bindingResult,
                                   RedirectAttributes redirectAttributes,
                                   Principal principal) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.educationStep", bindingResult);
            redirectAttributes.addFlashAttribute("educationStep", educationStep);
            return "redirect:/editor/forum/" + educationStep.getId() + "/edit";
        }
        User user = userService.getByUsername(principal.getName());
        educationStep.setUser(user);
        educationStep.setTime(getCurrentTime());
        educationStep.setDate(getCurrentDate());
        educationStepService.update(educationStep);
        return "redirect:/editor/forum";
    }

    @RequestMapping(value = "/{educ_id}/delete", method = RequestMethod.POST)
    public RedirectView educDelete(@PathVariable("educ_id") Long educ_id) {
        educationStepService.remove(educ_id);
        return new RedirectView("/editor/forum", true);
    }

    /*
     *                   COURSE
     */
    @RequestMapping(value = "/{educ_id}/add", method = RequestMethod.GET)
    public String add(Model model,
                      @PathVariable("educ_id") Long educ_id) {
        EducationStep educationStep = educationStepService.getById(educ_id);
        String title = educationStep.getName() + " - " + "Добавить курс";
        if (!model.containsAttribute("course")) {
            model.addAttribute("course", new Course());
        }
        model.addAttribute(CONTENT.toString(), action)
                .addAttribute("PAGE_TITLE", title)
                .addAttribute(MENU_OPTION.toString(), MenuOption.FORUM.name())
                .addAttribute("action", "add");
        return "editor/layouts/index";
    }

    @RequestMapping(value = "/{educ_id}/add", method = RequestMethod.POST)
    public String saveCourse(@Valid Course course,
                             BindingResult bindingResult,
                             @PathVariable("educ_id") Long educ_id,
                             RedirectAttributes redirectAttributes,
                             Principal principal) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.course", bindingResult);
            redirectAttributes.addFlashAttribute("course", course);
            return "redirect:/editor/forum/" + educ_id + "/add";
        }
        User user = userService.getByUsername(principal.getName());
        course.setUser(user);
        course.setTime(getCurrentTime());
        course.setDate(getCurrentDate());
        course.setEducationStep(educationStepService.getById(educ_id));
        courseService.add(course);
        return "redirect:/editor/forum";
    }

    @RequestMapping(value = "/{educ_id}/{course_id}/edit", method = RequestMethod.GET)
    public String editCourse(Model model,
                             @PathVariable("course_id") Long course_id) {
        Course course = courseService.getById(course_id);
        String title = "Изменить данные курса - " + course.getName();
        if (!model.containsAttribute("course")) {
            model.addAttribute("course", course);
        }
        model.addAttribute(CONTENT.toString(), action)
                .addAttribute("PAGE_TITLE", title)
                .addAttribute(MENU_OPTION.toString(), MenuOption.FORUM.name())
                .addAttribute("action", "edit");
        return "editor/layouts/index";
    }

    @RequestMapping(value = "/{educ_id}/{course_id}/edit", method = RequestMethod.POST)
    public String saveEditCourse(@Valid Course course,
                                 BindingResult bindingResult,
                                 @PathVariable("educ_id") Long educ_id,
                                 RedirectAttributes redirectAttributes,
                                 Principal principal) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.course", bindingResult);
            redirectAttributes.addFlashAttribute("course", course);
            return "redirect:/editor/forum/" + educ_id + "/" + course.getId() + "/edit";
        }
        User user = userService.getByUsername(principal.getName());
        course.setUser(user);
        course.setTime(getCurrentTime());
        course.setDate(getCurrentDate());
        course.setEducationStep(educationStepService.getById(educ_id));
        courseService.update(course);
        return "redirect:/editor/forum";
    }

    @RequestMapping(value = "/{educ_id}/{course_id}/delete", method = RequestMethod.POST)
    public RedirectView courseDelete(@PathVariable("course_id") Long course_id) {
        courseService.remove(course_id);
        return new RedirectView("/editor/forum", true);
    }

    /*
     *                   SPECIALIZATION
     */
    @RequestMapping(value = "/{educ_id}/{course_id}/add", method = RequestMethod.GET)
    public String add(Model model,
                      @PathVariable("educ_id") Long educ_id,
                      @PathVariable("course_id") Long course_id) {
        EducationStep educationStep = educationStepService.getById(educ_id);
        Course course = courseService.getById(course_id);
        String title = educationStep.getName() + " - " + course.getName() + " - " + "Добавить специализацию";
        if (!model.containsAttribute("specialization")) {
            model.addAttribute("specialization", new Specialization());
        }
        model.addAttribute(CONTENT.toString(), action)
                .addAttribute("PAGE_TITLE", title)
                .addAttribute(MENU_OPTION.toString(), MenuOption.FORUM.name())
                .addAttribute("action", "add");
        return "editor/layouts/index";
    }

    @RequestMapping(value = "/{educ_id}/{course_id}/add", method = RequestMethod.POST)
    public String saveSpec(@Valid Specialization specialization,
                           BindingResult bindingResult,
                           @PathVariable("educ_id") Long educ_id,
                           @PathVariable("course_id") Long course_id,
                           RedirectAttributes redirectAttributes,
                           Principal principal) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.specialization", bindingResult);
            redirectAttributes.addFlashAttribute("specialization", specialization);
            return "redirect:/editor/forum/" + educ_id + "/" + course_id + "/add";
        }
        User user = userService.getByUsername(principal.getName());
        specialization.setUser(user);
        specialization.setTime(getCurrentTime());
        specialization.setDate(getCurrentDate());
        specialization.setCourse(courseService.getById(course_id));
        specializationService.add(specialization);
        return "redirect:/editor/forum";
    }

    @RequestMapping(value = "/{educ_id}/{course_id}/{spec_id}/edit", method = RequestMethod.GET)
    public String editSpec(Model model,
                           @PathVariable("spec_id") Long spec_id) {
        Specialization specialization = specializationService.getById(spec_id);
        String title = "Изменить специализацию - " + specialization.getName();
        if (!model.containsAttribute("specialization")) {
            model.addAttribute("specialization", specialization);
        }
        model.addAttribute(CONTENT.toString(), action)
                .addAttribute("PAGE_TITLE", title)
                .addAttribute(MENU_OPTION.toString(), MenuOption.FORUM.name())
                .addAttribute("action", "edit");
        return "editor/layouts/index";
    }

    @RequestMapping(value = "/{educ_id}/{course_id}/{spec_id}/edit", method = RequestMethod.POST)
    public String saveEditSpecialization(@Valid Specialization specialization,
                                         BindingResult bindingResult,
                                         @PathVariable("educ_id") Long educ_id,
                                         @PathVariable("course_id") Long course_id,
                                         RedirectAttributes redirectAttributes,
                                         Principal principal) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.specialization", bindingResult);
            redirectAttributes.addFlashAttribute("specialization", specialization);
            return "redirect:/editor/forum/" + educ_id + "/" + course_id + "/" + specialization.getId() + "/edit";
        }
        User user = userService.getByUsername(principal.getName());
        specialization.setUser(user);
        specialization.setTime(getCurrentTime());
        specialization.setDate(getCurrentDate());
        specialization.setCourse(courseService.getById(course_id));
        specializationService.update(specialization);
        return "redirect:/editor/forum";
    }

    @RequestMapping(value = "/{educ_id}/{course_id}/{spec_id}/delete", method = RequestMethod.POST)
    public RedirectView specDelete(@PathVariable("spec_id") Long spec_id) {
        specializationService.remove(spec_id);
        return new RedirectView("/editor/forum", true);
    }

    /*
     *                   DISCIPLINE
     */
    @RequestMapping(value = "/{educ_id}/{course_id}/{spec_id}/add", method = RequestMethod.GET)
    public String add(Model model,
                      @PathVariable("educ_id") Long educ_id,
                      @PathVariable("course_id") Long course_id,
                      @PathVariable("spec_id") Long spec_id) {
        EducationStep educationStep = educationStepService.getById(educ_id);
        Course course = courseService.getById(course_id);
        Specialization specialization = specializationService.getById(spec_id);
        String title = educationStep.getName() + " - " + course.getName() + " - " + specialization.getName() + " - "
                + "Добавить дисциплину";
        if (!model.containsAttribute("discipline")) {
            model.addAttribute("discipline", new Discipline());
        }
        model.addAttribute(CONTENT.toString(), action)
                .addAttribute("PAGE_TITLE", title)
                .addAttribute(MENU_OPTION.toString(), MenuOption.FORUM.name())
                .addAttribute("action", "add");
        return "editor/layouts/index";
    }

    @RequestMapping(value = "/{educ_id}/{course_id}/{spec_id}/add", method = RequestMethod.POST)
    public String saveDisc(@Valid Discipline discipline,
                           BindingResult bindingResult,
                           @PathVariable("educ_id") Long educ_id,
                           @PathVariable("course_id") Long course_id,
                           @PathVariable("spec_id") Long spec_id,
                           RedirectAttributes redirectAttributes,
                           Principal principal) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.discipline", bindingResult);
            redirectAttributes.addFlashAttribute("discipline", discipline);
            return "redirect:/editor/forum/" + educ_id + "/" + course_id + "/" + spec_id + "/add";
        }
        User user = userService.getByUsername(principal.getName());
        discipline.setUser(user);
        discipline.setTime(getCurrentTime());
        discipline.setDate(getCurrentDate());
        discipline.setSpecialization(specializationService.getById(spec_id));
        disciplineService.add(discipline);
        return "redirect:/editor/forum";
    }


    @RequestMapping(value = "/{educ_id}/{course_id}/{spec_id}/{disc_id}/edit", method = RequestMethod.GET)
    public String editDisc(Model model,
                           @PathVariable("disc_id") Long disc_id) {
        Discipline discipline = disciplineService.getById(disc_id);
        String title = "Изменить данные предмета - " + discipline.getName();
        if (!model.containsAttribute("discipline")) {
            model.addAttribute("discipline", discipline);
        }
        model.addAttribute(CONTENT.toString(), action)
                .addAttribute("PAGE_TITLE", title)
                .addAttribute(MENU_OPTION.toString(), MenuOption.FORUM.name())
                .addAttribute("action", "edit");
        return "editor/layouts/index";
    }

    @RequestMapping(value = "/{educ_id}/{course_id}/{spec_id}/{disc_id}/edit", method = RequestMethod.POST)
    public String saveEditDiscipline(@Valid Discipline discipline,
                                     BindingResult bindingResult,
                                     @PathVariable("educ_id") Long educ_id,
                                     @PathVariable("course_id") Long course_id,
                                     @PathVariable("spec_id") Long spec_id,
                                     RedirectAttributes redirectAttributes,
                                     Principal principal) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.discipline", bindingResult);
            redirectAttributes.addFlashAttribute("discipline", discipline);
            return "redirect:/editor/forum/" + educ_id + "/" + course_id + "/" + spec_id + "/" + discipline.getId() + "/edit";
        }
        User user = userService.getByUsername(principal.getName());
        discipline.setUser(user);
        discipline.setTime(getCurrentTime());
        discipline.setDate(getCurrentDate());
        discipline.setSpecialization(specializationService.getById(spec_id));
        disciplineService.update(discipline);
        return "redirect:/editor/forum";
    }

    @RequestMapping(value = "/{educ_id}/{course_id}/{spec_id}/{disc_id}/delete", method = RequestMethod.POST)
    public RedirectView discDelete(@PathVariable("disc_id") Long disc_id) {
        disciplineService.remove(disc_id);
        return new RedirectView("/editor/forum", true);
    }

    /*
     *                   THREADS
     */
    @RequestMapping(value = "/{educ_id}/{course_id}/{spec_id}/{disc_id}", method = RequestMethod.GET)
    public String theadList(Model model,
                            @PathVariable("educ_id") Long educ_id,
                            @PathVariable("course_id") Long course_id,
                            @PathVariable("spec_id") Long spec_id,
                            @PathVariable("disc_id") Long disc_id,
                            Pageable pageable) {
        EducationStep educationStep = educationStepService.getById(educ_id);
        Course course = courseService.getById(course_id);
        Specialization specialization = specializationService.getById(spec_id);
        Discipline discipline = disciplineService.getById(disc_id);
        Page<Thread> threadPage = threadService.getThreadPage(disc_id, pageable.getPageNumber(), 10,
                Sort.Direction.DESC, "discipline_id","date", "time");
        PageWrapper<Thread> page = new PageWrapper<>(threadPage, disc_id.toString(), 10);

        model.addAttribute(CONTENT.toString(), threads)
                .addAttribute("list", page.getContent())
                .addAttribute("page", page)
                .addAttribute("PAGE_TITLE", discipline.getName() + " - Список тем")
                .addAttribute(MENU_OPTION.toString(), MenuOption.FORUM.name())
                .addAttribute("educ", educationStep)
                .addAttribute("course", course)
                .addAttribute("spec", specialization)
                .addAttribute("disc", discipline);
        return "editor/layouts/index";
    }

    @RequestMapping(value = "/{educ_id}/{course_id}/{spec_id}/{disc_id}/add", method = RequestMethod.GET)
    public String add(Model model,
                      @PathVariable("educ_id") Long educ_id,
                      @PathVariable("course_id") Long course_id,
                      @PathVariable("spec_id") Long spec_id,
                      @PathVariable("disc_id") Long disc_id) {
        EducationStep educationStep = educationStepService.getById(educ_id);
        Course course = courseService.getById(course_id);
        Specialization specialization = specializationService.getById(spec_id);
        Discipline discipline = disciplineService.getById(disc_id);
        String title = educationStep.getName() + " - " + course.getName() + " - " + specialization.getName() + " - " +
                discipline.getName() + " - " + "Добавить тему";
        if (!model.containsAttribute("thread")) {
            model.addAttribute("thread", new Thread());
        }
        model.addAttribute(CONTENT.toString(), action)
                .addAttribute("PAGE_TITLE", title)
                .addAttribute(MENU_OPTION.toString(), MenuOption.FORUM.name())
                .addAttribute("action", "add")
                .addAttribute("educ_id", educ_id)
                .addAttribute("course_id", course_id)
                .addAttribute("spec_id", spec_id)
                .addAttribute("disc_id", disc_id);
        return "editor/layouts/index";
    }

    @RequestMapping(value = "/{educ_id}/{course_id}/{spec_id}/{disc_id}/add", method = RequestMethod.POST)
    public String saveThread(@Valid Thread thread,
                             BindingResult bindingResult,
                             @PathVariable("educ_id") Long educ_id,
                             @PathVariable("course_id") Long course_id,
                             @PathVariable("spec_id") Long spec_id,
                             @PathVariable("disc_id") Long disc_id,
                             MultipartFile[] uploadingFiles,
                             RedirectAttributes redirectAttributes,
                             Principal principal) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.thread", bindingResult);
            redirectAttributes.addFlashAttribute("thread", thread);
            return "redirect:/editor/forum/" + educ_id + "/" + course_id + "/" + spec_id + "/" + disc_id + "/add";
        }
        User user = userService.getByUsername(principal.getName());
        thread.setUser(user);
        thread.setTime(getCurrentTime());
        thread.setDate(getCurrentDate());
        thread.setDiscipline(disciplineService.getById(disc_id));
        threadService.add(thread);
        if (uploadingFiles.length > 0) {
            for (MultipartFile file : uploadingFiles) {
                try {
                    if (!file.getOriginalFilename().isEmpty()) {
                        //TODO: SET UPDATED BY
                        File fileEntity = new File(file.getOriginalFilename(), user, getCurrentDate(), getCurrentTime());
                        String filename = fileManager.getName(threadFilesPath) + "." + fileManager.getExtension(file);
                        fileManager.create(file, threadFilesPath.toString(), filename);
                        fileEntity.setThread(thread);
                        fileEntity.setName(filename);
                        fileService.add(fileEntity);
                    }
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
        return "redirect:/editor/forum/" + educ_id + "/" + course_id + "/" + spec_id + "/" + disc_id;
    }


    @RequestMapping(value = "/{educ_id}/{course_id}/{spec_id}/{disc_id}/{thread_id}/edit", method = RequestMethod.GET)
    public String editThread(Model model,
                             @PathVariable("educ_id") Long educ_id,
                             @PathVariable("course_id") Long course_id,
                             @PathVariable("spec_id") Long spec_id,
                             @PathVariable("disc_id") Long disc_id,
                             @PathVariable("thread_id") Long thread_id) {
        Thread thread = threadService.getById(thread_id);
        String title = "Изменить тему - " + thread.getName();
        if (!model.containsAttribute("thread")) {
            model.addAttribute("thread", thread);
        }
        model.addAttribute(CONTENT.toString(), action)
                .addAttribute("PAGE_TITLE", title)
                .addAttribute(MENU_OPTION.toString(), MenuOption.FORUM.name())
                .addAttribute("action", "edit")
                .addAttribute("educ_id", educ_id)
                .addAttribute("course_id", course_id)
                .addAttribute("spec_id", spec_id)
                .addAttribute("disc_id", disc_id);
        return "editor/layouts/index";
    }

    @RequestMapping(value = "/{educ_id}/{course_id}/{spec_id}/{disc_id}/{thread_id}/edit", method = RequestMethod.POST)
    public String saveEditThread(@Valid Thread thread,
                                     BindingResult bindingResult,
                                     @PathVariable("educ_id") Long educ_id,
                                     @PathVariable("course_id") Long course_id,
                                     @PathVariable("spec_id") Long spec_id,
                                     @PathVariable("disc_id") Long disc_id,
                                     MultipartFile[] uploadingFiles,
                                     RedirectAttributes redirectAttributes,
                                     Principal principal) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.thread", bindingResult);
            redirectAttributes.addFlashAttribute("thread", thread);
            return "redirect:/editor/forum/" + educ_id + "/" + course_id + "/" + spec_id + "/" + disc_id + "/" +
                    thread.getId() + "/edit";
        }
        User user = userService.getByUsername(principal.getName());
        thread.setUser(user);
        thread.setTime(getCurrentTime());
        thread.setDate(getCurrentDate());
        thread.setDiscipline(disciplineService.getById(disc_id));
        threadService.update(thread);
        if (uploadingFiles.length > 0) {
            for (MultipartFile file : uploadingFiles) {
                try {
                    if (!file.getOriginalFilename().isEmpty()) {
                        //TODO: SET UPDATED BY
                        File fileEntity = new File(file.getOriginalFilename(), user, getCurrentDate(), getCurrentTime());
                        String filename = fileManager.getName(threadFilesPath) + "." + fileManager.getExtension(file);
                        fileManager.create(file, threadFilesPath.toString(), filename);
                        fileEntity.setThread(thread);
                        fileEntity.setName(filename);
                        fileService.add(fileEntity);
                    }
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
        return "redirect:/editor/forum/" + educ_id + "/" + course_id + "/" + spec_id + "/" + disc_id;
    }

    @RequestMapping(value = "/{educ_id}/{course_id}/{spec_id}/{disc_id}/{thread_id}/delete", method = RequestMethod.POST)
    public RedirectView threadDelete(@PathVariable("educ_id") Long educ_id,
                                     @PathVariable("course_id") Long course_id,
                                     @PathVariable("spec_id") Long spec_id,
                                     @PathVariable("disc_id") Long disc_id,
                                     @PathVariable("thread_id") Long thread_id) {
        threadService.remove(thread_id);
        return new RedirectView("/editor/forum/" + educ_id + "/" + course_id + "/" + spec_id + "/" + disc_id, true);
    }

    @PreAuthorize("hasAnyRole('MODER','ADMIN')")
    @RequestMapping(value = "/files/{file_id}/delete", method = RequestMethod.POST)
    public String fileDelete(@PathVariable("file_id") Long file_id) {
        fileService.remove(file_id);
        return "redirect:/";
    }
}
