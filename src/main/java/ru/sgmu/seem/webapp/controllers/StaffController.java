package ru.sgmu.seem.webapp.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.sgmu.seem.utils.enums.ListTitle;
import ru.sgmu.seem.utils.enums.MenuOption;
import ru.sgmu.seem.utils.enums.PageTitle;
import ru.sgmu.seem.webapp.domains.Staff;
import ru.sgmu.seem.webapp.services.StaffService;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static ru.sgmu.seem.utils.FolderManager.NEWS_IMAGES_URL;
import static ru.sgmu.seem.utils.FolderManager.STAFF_IMAGES_URL;
import static ru.sgmu.seem.utils.enums.FolderTitle.NEWS_IMAGES;
import static ru.sgmu.seem.utils.enums.FolderTitle.STAFF_IMAGES;
import static ru.sgmu.seem.utils.enums.ListTitle.STAFF_LIST;
import static ru.sgmu.seem.utils.enums.PageAttribute.*;

@Controller
@RequestMapping(value = "/staff")
public class StaffController {

    private Path staff = Paths.get("fragments", "staff", "staff");
    private Path staffItem = Paths.get("fragments", "staff", "item");

    private final StaffService staffService;

    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String staff(Model model) {
        Map<String, List<Staff>> mapStaff = staffService.getAll().stream()
                .sorted(Comparator.comparing(Staff::getSurname))
                .collect(Collectors.groupingBy(e -> e.getSurname().substring(0, 1)));
        model.addAttribute(CONTENT.name(), staff)
                .addAttribute(CURRENT_PAGE.name(), MenuOption.STAFF.name())
                .addAttribute(TITLE.name(), PageTitle.STAFF.getText())
                .addAttribute("STAFF_MAP", mapStaff);
        return "layouts/main";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String item(Model model,
                       @PathVariable("id") Long id) {
        Staff staff = staffService.getById(id);
        model.addAttribute(CONTENT.name(), staffItem)
                .addAttribute(CURRENT_PAGE.name(), MenuOption.STAFF.name())
                .addAttribute(TITLE.name(), PageTitle.STAFF.getText())
                .addAttribute("staff", staff)
                .addAttribute(STAFF_IMAGES.getText(), STAFF_IMAGES_URL);
        return "layouts/main";
    }
}
