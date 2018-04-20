package ru.sgmu.seem.webapp.controllers.editor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.sgmu.seem.utils.enums.MenuOption;
import ru.sgmu.seem.webapp.domains.*;
import ru.sgmu.seem.webapp.domains.Thread;
import ru.sgmu.seem.webapp.services.CrudService;
import ru.sgmu.seem.webapp.services.PageableService;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.sql.Time;
import java.util.*;

import static ru.sgmu.seem.utils.enums.PageAttribute.*;

@PreAuthorize("hasAnyRole('ADMIN','MODER')")
@Controller("EditorHomeController")
@RequestMapping(value = "/editor")
public class HomeController {

    private CrudService<Contact> contactService;
    private CrudService<Infoblock> infoblockService;
    private CrudService<Man> manService;
    private CrudService<Passage> passageService;
    private CrudService<Staff> staffService;
    private CrudService<EducationStep> educService;
    private CrudService<Course> courseService;
    private CrudService<Specialization> specService;
    private PageableService<Thread> threadService;

    private Path viewFragmentPath = Paths.get("editor", "fragments", "view");

    @Autowired
    public HomeController(PageableService<Thread> threadService,
                          @Qualifier("infoblockService") CrudService<Infoblock> infoblockService,
                          @Qualifier("contactService") CrudService<Contact> contactService,
                          @Qualifier("manService") CrudService<Man> manService,
                          @Qualifier("passageService") CrudService<Passage> passageService,
                          @Qualifier("staffService") CrudService<Staff> staffService,
                          @Qualifier("educationStepService") CrudService<EducationStep> educService,
                          @Qualifier("courseService") CrudService<Course> courseService,
                          @Qualifier("specializationService") CrudService<Specialization> specService) {
        this.threadService = threadService;
        this.infoblockService = infoblockService;
        this.contactService = contactService;
        this.manService = manService;
        this.passageService = passageService;
        this.staffService = staffService;
        this.educService = educService;
        this.courseService = courseService;
        this.specService = specService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String index(Model model) {
        List<EntityDetails> changes = new ArrayList<>();
        changes.addAll(contactService.getAll());
        changes.addAll(infoblockService.getAll());
        //changes.addAll(threadService.getAll());       // ADD THREADS
        changes.addAll(contactService.getAll());
        changes.addAll(manService.getAll());
        changes.addAll(passageService.getAll());
        changes.addAll(staffService.getAll());
        changes.addAll(educService.getAll());
        changes.addAll(courseService.getAll());
        changes.addAll(specService.getAll());
        changes.sort(Comparator.comparing(EntityDetails::getDate).thenComparing(EntityDetails::getTime).reversed());
        changes = changes.subList(0,10);
        model.addAttribute(CONTENT.name(), viewFragmentPath)
                .addAttribute("list", changes)
                .addAttribute(MENU_OPTION.name(), MenuOption.VIEW.name());
        return "editor/layouts/index";
    }

}
