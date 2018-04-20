package ru.sgmu.seem.webapp.controllers.editor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.sgmu.seem.utils.ImageManager;
import ru.sgmu.seem.utils.enums.MenuOption;
import ru.sgmu.seem.webapp.domains.Infoblock;
import ru.sgmu.seem.webapp.domains.Man;
import ru.sgmu.seem.webapp.domains.User;
import ru.sgmu.seem.webapp.services.CrudService;
import ru.sgmu.seem.webapp.services.CustomUserDetailsService;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.List;

import static ru.sgmu.seem.utils.DateManager.getCurrentDate;
import static ru.sgmu.seem.utils.DateManager.getCurrentTime;
import static ru.sgmu.seem.utils.FolderManager.*;
import static ru.sgmu.seem.utils.FormValidator.IMAGE_UPLOAD_ERROR;
import static ru.sgmu.seem.utils.enums.ListTitle.INFOBLOCK_LIST;
import static ru.sgmu.seem.utils.enums.ListTitle.MAN_LIST;
import static ru.sgmu.seem.utils.enums.FolderTitle.INFOBLOCK_IMAGES;
import static ru.sgmu.seem.utils.enums.FolderTitle.MAN_IMAGES;
import static ru.sgmu.seem.utils.enums.PageAttribute.*;

@PreAuthorize("hasRole('ADMIN')")
@Controller("EditorMainController")
@RequestMapping(value = "/editor/main")
public class MainController {

    private Path main = Paths.get("editor", "fragments", "main", "main");
    private Path editMan = Paths.get("editor", "fragments", "main", "edit-man");
    private Path editInfoblock = Paths.get("editor", "fragments", "main", "edit-infoblock");
    private CrudService<Man> manService;
    private CrudService<Infoblock> infoblockService;
    private ImageManager imageManager;
    private CustomUserDetailsService userService;

    @Autowired
    public MainController(CrudService<Man> manService,
                          CrudService<Infoblock> infoblockService,
                          ImageManager imageManager,
                          CustomUserDetailsService userService) {
        this.manService = manService;
        this.infoblockService = infoblockService;
        this.imageManager = imageManager;
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String main(Model model) {
        List<Man> manList = manService.getAll();
        List<Infoblock> infoblockList = infoblockService.getAll();
        manList.sort((e1, e2) -> (Long.compare(e1.getId(), e2.getId())));
        infoblockList.sort((e1, e2) -> (Long.compare(e1.getId(), e2.getId())));
        model.addAttribute(MAN_LIST.name(), manList)
                .addAttribute(INFOBLOCK_LIST.name(), infoblockList)
                .addAttribute(CONTENT.toString(), main)
                .addAttribute(MENU_OPTION.toString(), MenuOption.MAIN.name())
                .addAttribute(MAN_IMAGES.getText(), MAN_IMAGES_URL)
                .addAttribute(INFOBLOCK_IMAGES.getText(), INFOBLOCK_IMAGES_URL);
        return "editor/layouts/index";
    }

    @RequestMapping(value = "/man/{id}", method = RequestMethod.GET)
    public String mainMan(@PathVariable("id") Long id,
                       Model model) {
        if (!model.containsAttribute("man")) {
            Man man = manService.getById(id);
            model.addAttribute("man", man);
        }
        model.addAttribute(CONTENT.toString(), editMan)
                .addAttribute(MAN_IMAGES.getText(), MAN_IMAGES_URL)
                .addAttribute(MENU_OPTION.toString(), MenuOption.MAIN.name());
        return "editor/layouts/index";
    }

    @RequestMapping(value = "/man/save", method = RequestMethod.POST)
    public String main(@Valid Man man,
                       BindingResult bindingResult,
                       @RequestParam("image") MultipartFile file,
                       RedirectAttributes redirectAttributes,
                       Principal principal) throws IOException{
        String oldImageName = manService.getById(man.getId()).getImageName();
        String imageName = oldImageName;
        if (!file.isEmpty()) {
            if (!imageManager.checkExtention(file)) {
                redirectAttributes.addFlashAttribute("image_err", IMAGE_UPLOAD_ERROR);
            } else {
                imageName = imageManager.getImageName(file, manImagesPath);
            }
        }
        if (bindingResult.hasErrors() || !redirectAttributes.getFlashAttributes().isEmpty()) {
            man.setImageName(oldImageName);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.man", bindingResult);
            redirectAttributes.addFlashAttribute("man", man);
            return "redirect:/editor/main/man/" + man.getId();
        }
        if (imageName != null && oldImageName != null && !imageName.equals(oldImageName)) {
            imageManager.deleteImage(Paths.get(manImagesPath.toString(), oldImageName));
        }
        man.setImageName(imageName);
        User user = userService.getByUsername(principal.getName());
        man.setUser(user);
        man.setDate(getCurrentDate());
        man.setTime(getCurrentTime());
        manService.update(man);
        return "redirect:/editor/main";
    }

    @RequestMapping(value = "/infoblock/{id}", method = RequestMethod.GET)
    public String main(@PathVariable("id") Long id,
                       Model model) {
        if (!model.containsAttribute("infoblock")) {
            Infoblock infoblock = infoblockService.getById(id);
            model.addAttribute("infoblock", infoblock);
        }
        model.addAttribute(CONTENT.toString(), editInfoblock)
                .addAttribute(INFOBLOCK_IMAGES.getText(), INFOBLOCK_IMAGES_URL)
                .addAttribute(MENU_OPTION.toString(), MenuOption.MAIN.name());
        return "editor/layouts/index";
    }

    @RequestMapping(value = "/infoblock/save", method = RequestMethod.POST)
    public String main(@Valid Infoblock infoblock,
                       BindingResult bindingResult,
                       @RequestParam("image") MultipartFile file,
                       RedirectAttributes redirectAttributes,
                       Principal principal) throws IOException{
        String oldImageName = infoblockService.getById(infoblock.getId()).getImageName();
        String imageName = oldImageName;
        if (!file.isEmpty()) {
            if (!imageManager.checkExtention(file)) {
                redirectAttributes.addFlashAttribute("image_err", IMAGE_UPLOAD_ERROR);
            } else {
                imageName = imageManager.getImageName(file, infoblockImagesPath);
            }
        }
        if (bindingResult.hasErrors() || !redirectAttributes.getFlashAttributes().isEmpty()) {
            infoblock.setImageName(oldImageName);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.infoblock", bindingResult);
            redirectAttributes.addFlashAttribute("infoblock", infoblock);
            return "redirect:/editor/main/infoblock/" + infoblock.getId();
        }
        if (imageName != null && oldImageName != null && !imageName.equals(oldImageName)) {
            imageManager.deleteImage(Paths.get(infoblockImagesPath.toString(), oldImageName));
        }
        infoblock.setImageName(imageName);
        User user = userService.getByUsername(principal.getName());
        infoblock.setUser(user);
        infoblock.setDate(getCurrentDate());
        infoblock.setTime(getCurrentTime());
        infoblockService.update(infoblock);
        return "redirect:/editor/main";
    }
}
