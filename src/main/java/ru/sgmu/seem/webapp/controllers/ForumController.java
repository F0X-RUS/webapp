package ru.sgmu.seem.webapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.sgmu.seem.utils.FileManager;
import ru.sgmu.seem.utils.PageWrapper;
import ru.sgmu.seem.webapp.domains.*;
import ru.sgmu.seem.webapp.domains.Thread;
import ru.sgmu.seem.webapp.services.*;
import ru.sgmu.seem.webapp.domains.File;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

import static ru.sgmu.seem.utils.DateManager.getCurrentDate;
import static ru.sgmu.seem.utils.DateManager.getCurrentTime;
import static ru.sgmu.seem.utils.FolderManager.threadFilesPath;
import static ru.sgmu.seem.utils.enums.PageAttribute.*;
import static ru.sgmu.seem.utils.enums.MenuOption.*;

@Controller
@RequestMapping(value = "/forum")
public class ForumController {

    private static final String ACTION = "{action}";
    private Path forumLayout = Paths.get("fragments", "forum", "layouts", "forum");
    private Path forumMain = Paths.get("fragments", "forum", "forum");
    private Path coursesFragment = Paths.get("fragments", "forum", "courses");
    private Path threadFragment = Paths.get("fragments", "forum", "thread");
    private Path addThreadFragment = Paths.get("fragments", "forum", "add-thread");
    private Path editThreadFragment = Paths.get("fragments", "forum", "edit-thread");
    private CrudService<EducationStep> educService;
    private CrudService<Course> courseService;
    private CrudService<Specialization> specService;
    private CrudService<Discipline> discService;
    private CrudService<File> fileService;
    private ThreadService threadCrudService;
    private FileManager fileManager;
    private CustomUserDetailsService userService;

    @Autowired
    public ForumController(@Qualifier("educationStepService") CrudService<EducationStep> educService,
                           @Qualifier("courseService") CrudService<Course> courseService,
                           @Qualifier("specializationService") CrudService<Specialization> specService,
                           @Qualifier("disciplineService") CrudService<Discipline> discService,
                           @Qualifier("threadService") ThreadService threadCrudService,
                           @Qualifier("fileService") CrudService<File> fileService,
                           FileManager fileManager,
                           CustomUserDetailsService userService) {
        this.educService = educService;
        this.courseService = courseService;
        this.specService = specService;
        this.discService = discService;
        this.threadCrudService = threadCrudService;
        this.fileManager = fileManager;
        this.fileService = fileService;
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String educationSteps(Model model) {
        List<EducationStep> educationSteps = educService.getAll();
        educationSteps.sort(Comparator.comparing(EntityDetails::getId));
        model.addAttribute(CONTENT.name(), forumLayout)
                .addAttribute("FORUM_CONTENT", forumMain)
                .addAttribute(MENU_OPTION.name(), FORUM.name())
                .addAttribute("list", educationSteps);
        return "layouts/main";
    }

    @RequestMapping(value = "/{educ_id}", method = RequestMethod.GET)
    public String courses(Model model,
                          @PathVariable("educ_id") Long educ_id) {
        SortedSet<Course> courses = new TreeSet<>(Comparator.comparing(EntityDetails::getId));
        courses.addAll(educService.getById(educ_id).getCourses());
        model.addAttribute(CONTENT.name(), forumLayout)
                .addAttribute("FORUM_CONTENT", coursesFragment)
                .addAttribute(MENU_OPTION.name(), FORUM.name())
                .addAttribute("list", courses)
                .addAttribute("current_id", educ_id)
                .addAttribute("educ", educService.getById(educ_id));
        return "layouts/main";
    }


    //TODO: REMADE
    @RequestMapping(value = "/{educ_id}/{course_id}", method = RequestMethod.GET)
    public String specializations(Model model,
                                  @PathVariable("educ_id") Long educ_id,
                                  @PathVariable("course_id") Long course_id) {
        Course course = courseService.getById(course_id);
        SortedSet<Specialization> courseSpecList = new TreeSet<>(Comparator.comparing(EntityDetails::getId));
        courseSpecList.addAll(course.getSpecializations());
        model.addAttribute(CONTENT.name(), forumLayout)
                .addAttribute("FORUM_CONTENT", coursesFragment)
                .addAttribute(MENU_OPTION.name(), FORUM.name())
                .addAttribute("list", courseSpecList)
                .addAttribute("current_id", course_id)
                .addAttribute("educ", educService.getById(educ_id))
                .addAttribute("course", courseService.getById(course_id));
        return "layouts/main";
    }

    @RequestMapping(value = "/{educ_id}/{course_id}/{spec_id}", method = RequestMethod.GET)
    public String disciplines(Model model,
                              @PathVariable("educ_id") Long educ_id,
                              @PathVariable("course_id") Long course_id,
                              @PathVariable("spec_id") Long spec_id) {
        EducationStep educationStep = educService.getById(educ_id);
        Course course = courseService.getById(course_id);
        Specialization specialization = specService.getById(spec_id);
        SortedSet<Discipline> discList = new TreeSet<>(Comparator.comparing(EntityDetails::getId));
        discList.addAll(specialization.getDisciplines());
        model.addAttribute(CONTENT.name(), forumLayout)
                .addAttribute("FORUM_CONTENT", coursesFragment)
                .addAttribute(MENU_OPTION.name(), FORUM.name())
                .addAttribute("list", discList)
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
                          @PathVariable("disc_id") Long disc_id,
                          Pageable pageable) {
        EducationStep educationStep = educService.getById(educ_id);
        Course course = courseService.getById(course_id);
        Specialization specialization = specService.getById(spec_id);
        Discipline discipline = discService.getById(disc_id);
        Page<Thread> threadPage = threadCrudService.getThreadPage(disc_id, pageable.getPageNumber(), 10,
                Sort.Direction.DESC, "discipline_id","date", "time");
        PageWrapper<Thread> page = new PageWrapper<>(threadPage, disc_id.toString(), 10);
        model.addAttribute(CONTENT.name(), forumLayout)
                .addAttribute("page", page)
                .addAttribute("FORUM_CONTENT", coursesFragment)
                .addAttribute(MENU_OPTION.name(), FORUM.name())
                .addAttribute("list", page.getContent())
                .addAttribute("current_id", disc_id)
                .addAttribute("educ", educationStep)
                .addAttribute("course", course)
                .addAttribute("spec", specialization)
                .addAttribute("disc", discipline);
        return "layouts/main";
    }

    @PreAuthorize("hasAnyRole('USER','TEACHER','MODER','ADMIN')")
    @RequestMapping(value = "/{educ_id}/{course_id}/{spec_id}/{disc_id}/{thread_id}", method = RequestMethod.GET)
    public String thread(Model model,
                         @PathVariable("educ_id") Long educ_id,
                         @PathVariable("course_id") Long course_id,
                         @PathVariable("spec_id") Long spec_id,
                         @PathVariable("disc_id") Long disc_id,
                         @PathVariable("thread_id") Long thread_id) {
        Thread currentThread = threadCrudService.getById(thread_id);
        EducationStep educationStep = educService.getById(educ_id);
        Course course = courseService.getById(course_id);
        Specialization specialization = specService.getById(spec_id);
        Discipline discipline = discService.getById(disc_id);
        SortedSet<Post> posts = new TreeSet<>(Comparator.comparing(Post::getId));
        posts.addAll(currentThread.getPosts());
        if (!model.containsAttribute("post")) {
            model.addAttribute("post", new Post());
        }
        model.addAttribute(CONTENT.name(), forumLayout)
                .addAttribute("FORUM_CONTENT", threadFragment)
                .addAttribute(MENU_OPTION.name(), FORUM.name())
                .addAttribute("thread", currentThread)
                .addAttribute("educ", educationStep)
                .addAttribute("course", course)
                .addAttribute("spec", specialization)
                .addAttribute("disc", discipline)
                .addAttribute("files", currentThread.getFiles())
                .addAttribute("posts", posts);
        return "layouts/main";
    }

    @PreAuthorize("hasAnyRole('TEACHER','MODER','ADMIN')")
    @RequestMapping(value = "/{educ_id}/{course_id}/{spec_id}/{disc_id}/add", method = RequestMethod.GET)
    public String threadAdd(Model model,
                            @PathVariable("educ_id") Long educ_id,
                            @PathVariable("course_id") Long course_id,
                            @PathVariable("spec_id") Long spec_id,
                            @PathVariable("disc_id") Long disc_id) {
        if (!model.containsAttribute("thread")) {
            model.addAttribute("thread", new Thread());
        }
        model.addAttribute(CONTENT.name(), forumLayout)
                .addAttribute("FORUM_CONTENT", addThreadFragment)
                .addAttribute(MENU_OPTION.name(), FORUM.name())
                .addAttribute("educ", educService.getById(educ_id))
                .addAttribute("course", courseService.getById(course_id))
                .addAttribute("spec", specService.getById(spec_id))
                .addAttribute("disc", discService.getById(disc_id))
                .addAttribute("lang", LocaleContextHolder.getLocale().toString().split("-")[0]);
        return "layouts/main";
    }

    @PreAuthorize("hasAnyRole('TEACHER','MODER','ADMIN')")
    @RequestMapping(value = "/{educ_id}/{course_id}/{spec_id}/{disc_id}/{thread_id}/edit", method = RequestMethod.GET)
    public String threadEdit(Model model,
                             @PathVariable("educ_id") Long educ_id,
                             @PathVariable("course_id") Long course_id,
                             @PathVariable("spec_id") Long spec_id,
                             @PathVariable("disc_id") Long disc_id,
                             @PathVariable("thread_id") Long thread_id) {
        if (!model.containsAttribute("thread")) {
            model.addAttribute("thread", threadCrudService.getById(thread_id));
        }
        model.addAttribute(CONTENT.name(), forumLayout)
                .addAttribute("FORUM_CONTENT", editThreadFragment)
                .addAttribute(MENU_OPTION.name(), FORUM.name())
                .addAttribute("educ", educService.getById(educ_id))
                .addAttribute("course", courseService.getById(course_id))
                .addAttribute("spec", specService.getById(spec_id))
                .addAttribute("disc", discService.getById(disc_id));
        return "layouts/main";
    }

    @PreAuthorize("hasAnyRole('TEACHER','MODER','ADMIN')")
    @RequestMapping(value = {"/{educ_id}/{course_id}/{spec_id}/{disc_id}/add",
            "/{educ_id}/{course_id}/{spec_id}/{disc_id}/{thread_id}/edit"}, method = RequestMethod.POST)
    public String threadSave(@Valid Thread thread,
                             BindingResult bindingResult,
                             @PathVariable("educ_id") Long educ_id,
                             @PathVariable("course_id") Long course_id,
                             @PathVariable("spec_id") Long spec_id,
                             @PathVariable("disc_id") Long disc_id,
                             @PathVariable("thread_id") Optional<Long> thread_id,
                             MultipartFile[] uploadingFiles,
                             RedirectAttributes redirectAttributes,
                             Principal principal) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("thread", thread);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.thread", bindingResult);
            return thread_id.map(id -> "redirect:/forum/" + educ_id + "/" + course_id + "/" + spec_id + "/"
                    + disc_id + "/" + id + "/edit")
                    .orElseGet(() -> "redirect:/forum/" + educ_id + "/" + course_id + "/" + spec_id + "/" + disc_id
                            + "/add");
        }
        thread.setDate(getCurrentDate());
        thread.setTime(getCurrentTime());
        User user = userService.getByUsername(principal.getName());
        thread.setUser(user);
        thread.setDiscipline(discService.getById(disc_id));
        thread.setFiles(fileService.getAll().stream()
                .filter(e -> e.getThread().equals(thread))
                .collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparingLong(File::getId)))));
        threadCrudService.add(thread);
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
        return "redirect:/forum/" + educ_id + "/" + course_id + "/" + spec_id + "/" + disc_id + "/" + thread.getId();
    }

    @PreAuthorize("hasAnyRole('TEACHER','MODER','ADMIN')")
    @RequestMapping(value = "/{educ_id}/{course_id}/{spec_id}/{disc_id}/{thread_id}/delete", method = RequestMethod.POST)
    public String threadDelete(@PathVariable("thread_id") Long thread_id,
                               @PathVariable("educ_id") Long educ_id,
                               @PathVariable("course_id") Long course_id,
                               @PathVariable("spec_id") Long spec_id,
                               @PathVariable("disc_id") Long disc_id) {
        threadCrudService.remove(thread_id);
        return "redirect:/forum/" + educ_id + "/" + course_id + "/" + spec_id + "/" + disc_id;
    }


    @PreAuthorize("hasAnyRole('TEACHER','MODER','ADMIN')")
    @RequestMapping(value = "/files/{file_id}/delete", method = RequestMethod.POST)
    public String fileDelete(@PathVariable("file_id") Long file_id) {
        fileService.remove(file_id);
        return "redirect:/";
    }
}
