package ru.sgmu.seem.webapp.controllers.editor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import ru.sgmu.seem.utils.FormValidator;
import ru.sgmu.seem.utils.ImageManager;
import ru.sgmu.seem.utils.enums.ListTitle;
import ru.sgmu.seem.utils.enums.MenuOption;
import ru.sgmu.seem.webapp.domains.EntityDetails;
import ru.sgmu.seem.webapp.domains.Staff;
import ru.sgmu.seem.webapp.domains.User;
import ru.sgmu.seem.webapp.services.CustomUserDetailsService;
import ru.sgmu.seem.webapp.services.StaffService;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.Comparator;
import java.util.List;

import static ru.sgmu.seem.utils.DateManager.getCurrentDate;
import static ru.sgmu.seem.utils.DateManager.getCurrentTime;
import static ru.sgmu.seem.utils.FolderManager.STAFF_IMAGES_URL;
import static ru.sgmu.seem.utils.FolderManager.staffImagesPath;
import static ru.sgmu.seem.utils.FormValidator.IMAGE_UPLOAD_ERROR;
import static ru.sgmu.seem.utils.enums.FolderTitle.STAFF_IMAGES;
import static ru.sgmu.seem.utils.enums.PageAttribute.*;
import static ru.sgmu.seem.utils.enums.PageAttribute.MENU_OPTION;

@PreAuthorize("hasRole('ADMIN')")
@Controller("EditorStaffController")
@RequestMapping(value = "/editor/staff")
public class StaffController {

    private Path staffFragmentPath = Paths.get("editor", "fragments", "staff", "staff");
    private Path addStaff = Paths.get("editor", "fragments", "staff", "add");
    private Path editStaff = Paths.get("editor", "fragments", "staff", "edit");
    private StaffService staffService;
    private ImageManager imageManager;
    private CustomUserDetailsService userService;
    private FormValidator formValidator;

    @Autowired
    public StaffController(StaffService staffService,
                           ImageManager imageManager,
                           CustomUserDetailsService userService,
                           FormValidator formValidator) {
        this.staffService = staffService;
        this.imageManager = imageManager;
        this.userService = userService;
        this.formValidator = formValidator;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String staff(Model model) {
        List<Staff> list = staffService.getAll();
        list.sort(Comparator.comparing(EntityDetails::getDate).thenComparing(EntityDetails::getTime).reversed());
        model.addAttribute(CONTENT.name(), staffFragmentPath)
                .addAttribute(MENU_OPTION.toString(), MenuOption.STAFF.name())
                .addAttribute(ListTitle.STAFF_LIST.name(), list)
                .addAttribute(STAFF_IMAGES.getText(), STAFF_IMAGES_URL);
        return "editor/layouts/index";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(Model model) {
        if (!model.containsAttribute("staff")) {
            model.addAttribute("staff", new Staff());
        }
        model.addAttribute(CONTENT.name(), addStaff)
                .addAttribute(MENU_OPTION.toString(), MenuOption.STAFF.name())
                .addAttribute("lang", LocaleContextHolder.getLocale().toString().split("-")[0]);
        return "editor/layouts/index";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String add(@Valid Staff staff,
                      BindingResult bindingResult,
                      @RequestParam("image") MultipartFile file,
                      Model model,
                      RedirectAttributes redirectAttributes,
                      Principal principal) {
        if (!file.isEmpty() || bindingResult.hasErrors()) {
            if (!formValidator.checkImage(file, bindingResult, model)) {
                if (!imageManager.checkSelectedImage(file)) {
                    redirectAttributes.addFlashAttribute("image_err", IMAGE_UPLOAD_ERROR);
                }
                redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.staff", bindingResult);
                redirectAttributes.addFlashAttribute("staff", staff);
                return "redirect:/editor/staff/add";
            }
            String imageName = imageManager.getImageName(file, staffImagesPath);
            staff.setImageName(imageName);
        }
        User user = userService.getByUsername(principal.getName());
        staff.setUser(user);
        staff.setDate(getCurrentDate());
        staff.setTime(getCurrentTime());
        staffService.add(staff);
        return "redirect:/editor/staff";
    }

    @RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
    public String staffEdit(Model model,
                            @PathVariable("id") Long id) {
        if (!model.containsAttribute("staff")) {
            model.addAttribute("staff", staffService.getById(id));
        }
        model.addAttribute(CONTENT.toString(), editStaff)
                .addAttribute(STAFF_IMAGES.getText(), STAFF_IMAGES_URL)
                .addAttribute(MENU_OPTION.name(), MenuOption.STAFF.toString())
                .addAttribute("lang", LocaleContextHolder.getLocale().toString().split("-")[0]);
        return "editor/layouts/index";
    }

    @RequestMapping(value = "/edit", params = "submit", method = RequestMethod.POST)
    public String staffEditSubmit(@Valid Staff staff,
                                  BindingResult bindingResult,
                                  @RequestParam("image") MultipartFile file,
                                  @Param("clear") boolean clear,
                                  RedirectAttributes redirectAttributes,
                                  Principal principal) throws IOException {
        String oldImageName = staffService.getById(staff.getId()).getImageName();
        String imageName = oldImageName;
        if (!file.isEmpty()) {
            if (!imageManager.checkExtention(file)) {
                redirectAttributes.addFlashAttribute("image_err", IMAGE_UPLOAD_ERROR);
            } else {
                imageName = imageManager.getImageName(file, staffImagesPath);
            }
        }
        if (bindingResult.hasErrors() || !redirectAttributes.getFlashAttributes().isEmpty()) {
            staff.setImageName(oldImageName);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.staff", bindingResult);
            redirectAttributes.addFlashAttribute("staff", staff);
            return "redirect:/editor/staff/" + staff.getId() + "/edit";
        }
        if (imageName != null && oldImageName != null && !imageName.equals(oldImageName)) {
            imageManager.deleteImage(Paths.get(staffImagesPath.toString(), oldImageName));
        }
        if (clear) {
            imageName = null;
            imageManager.deleteImage(Paths.get(staffImagesPath.toString(), oldImageName));
        }
        staff.setImageName(imageName);
        User user = userService.getByUsername(principal.getName());
        staff.setUser(user);
        staff.setDate(getCurrentDate());
        staff.setTime(getCurrentTime());
        staffService.update(staff);
        return "redirect:/editor/staff";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public RedirectView newsDelete(@RequestParam("id") Long id) {
        staffService.remove(id);
        return new RedirectView("/editor/staff", true);
    }
}
