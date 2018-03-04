package ru.sgmu.seem.webapp.controllers.editor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;
import ru.sgmu.seem.utils.FormValidator;
import ru.sgmu.seem.utils.ImageManager;
import ru.sgmu.seem.utils.enums.FolderTitle;
import ru.sgmu.seem.utils.enums.ListTitle;
import ru.sgmu.seem.utils.enums.MenuOption;
import ru.sgmu.seem.webapp.domains.Staff;
import ru.sgmu.seem.webapp.services.StaffService;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static ru.sgmu.seem.utils.DateManager.getCurrentDate;
import static ru.sgmu.seem.utils.DateManager.getCurrentTime;
import static ru.sgmu.seem.utils.FolderManager.STAFF_IMAGES_URL;
import static ru.sgmu.seem.utils.FolderManager.staffImagesPath;
import static ru.sgmu.seem.utils.FormValidator.IMAGE_UPLOAD_ERROR;
import static ru.sgmu.seem.utils.enums.FolderTitle.STAFF_IMAGES;
import static ru.sgmu.seem.utils.enums.PageAttribute.*;
import static ru.sgmu.seem.utils.enums.PageAttribute.CURRENT_PAGE;
import static ru.sgmu.seem.utils.enums.PageTitle.*;

@Controller("EditorStaffController")
@RequestMapping(value = "/editor/staff")
public class StaffController {

    private Path staffFragmentPath = Paths.get("editor", "fragments", "staff", "staff");
    private Path addStaff = Paths.get("editor", "fragments", "staff", "add");
    private Path editStaff = Paths.get("editor", "fragments", "staff", "edit");
    private StaffService staffService;
    private FormValidator formValidator;
    private ImageManager imageManager;

    @Autowired
    public StaffController(StaffService staffService, FormValidator formValidator, ImageManager imageManager) {
        this.staffService = staffService;
        this.formValidator = formValidator;
        this.imageManager = imageManager;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String staff(Model model) {
        List<Staff> list = staffService.getAll();
        model.addAttribute(CONTENT.name(), staffFragmentPath)
                .addAttribute(MENU_OPTION.toString(), MenuOption.STAFF.name())
                .addAttribute(TITLE.name(), STAFF.getText())
                .addAttribute(ListTitle.STAFF_LIST.name(), list)
                .addAttribute(STAFF_IMAGES.getText(), STAFF_IMAGES_URL);
        return "/editor/layouts/index";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(Model model,
                      Staff staff) {
        model.addAttribute(CONTENT.name(), addStaff)
                .addAttribute(MENU_OPTION.toString(), MenuOption.STAFF.name())
                .addAttribute(TITLE.name(), STAFF.getText());
        return "/editor/layouts/index";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String add(@Valid Staff staff,
                      BindingResult bindingResult,
                      @RequestParam("image") MultipartFile file,
                      Model model) {
        if (!formValidator.check(file, bindingResult, model)) {
            model.addAttribute(CONTENT.name(), addStaff)
                    .addAttribute(MENU_OPTION.toString(), MenuOption.STAFF.name())
                    .addAttribute(TITLE.name(), ADD_STAFF.getText());
            return "/editor/layouts/index";
        }
        String imageName = imageManager.getImageName(file, staffImagesPath);
        staff.setImageName(imageName);
        staff.setDate(getCurrentDate());
        staff.setTime(getCurrentTime());
        staffService.add(staff);
        return "redirect:/editor/staff";
    }

    @RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
    public String staffEdit(Model model,
                            @PathVariable("id") Long id,
                            Staff staff) {
        staff = staffService.getById(id);
        model.addAttribute(CONTENT.toString(), editStaff)
                .addAttribute(TITLE.toString(), EDIT_STAFF.getText())
                .addAttribute(STAFF_IMAGES.getText(), STAFF_IMAGES_URL)
                .addAttribute(CURRENT_PAGE.name(), MenuOption.STAFF.toString())
                .addAttribute("staff", staff);
        return "editor/layouts/index";
    }

    @RequestMapping(value = "/edit", params = "submit", method = RequestMethod.POST)
    public String staffEditSubmit(@Valid Staff staff,
                                  BindingResult bindingResult,
                                  @RequestParam("image") MultipartFile file,
                                  Model model) {
        String oldImageName = staffService.getById(staff.getId()).getImageName();
        String fileExtention = file.getContentType().split("/")[1].toUpperCase();
        if (!imageManager.checkExtention(fileExtention) && !file.isEmpty()){
            model.addAttribute("image_err", IMAGE_UPLOAD_ERROR);
        }
        if (bindingResult.hasErrors() || model.containsAttribute("image_err")) {
            staff.setImageName(oldImageName);
            model.addAttribute(CONTENT.name(), editStaff)
                    .addAttribute(TITLE.name(), EDIT_STAFF.getText())
                    .addAttribute(CURRENT_PAGE.name(), MenuOption.STAFF.toString())
                    .addAttribute("staff", staff)
                    .addAttribute(FolderTitle.STAFF_IMAGES.getText(), STAFF_IMAGES_URL);
            return "/editor/layouts/index";
        }
        String imageName = (!file.isEmpty()) ? imageManager.getImageName(file, staffImagesPath) : oldImageName;
        if (!imageName.equals(oldImageName)){
            try {
                imageManager.deleteImage(Paths.get(staffImagesPath.toString(), oldImageName));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        staff.setImageName(imageName);
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
