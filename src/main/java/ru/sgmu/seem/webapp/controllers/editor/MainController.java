package ru.sgmu.seem.webapp.controllers.editor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.sgmu.seem.utils.DateManager;
import ru.sgmu.seem.utils.FormValidator;
import ru.sgmu.seem.utils.ImageManager;
import ru.sgmu.seem.utils.enums.MenuOption;
import ru.sgmu.seem.webapp.domains.Infoblock;
import ru.sgmu.seem.webapp.domains.Man;
import ru.sgmu.seem.webapp.services.CrudService;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.Map;

import static ru.sgmu.seem.utils.FolderManager.INFOBLOCK_IMAGES_URL;
import static ru.sgmu.seem.utils.FolderManager.MAN_IMAGES_URL;
import static ru.sgmu.seem.utils.FormValidator.IMAGE_ERROR;
import static ru.sgmu.seem.utils.FormValidator.IMAGE_WRONG_FORMAT;
import static ru.sgmu.seem.utils.enums.PageAttribute.CONTENT;
import static ru.sgmu.seem.utils.enums.PageAttribute.MENU_OPTION;
import static ru.sgmu.seem.utils.enums.PageAttribute.TITLE;
import static ru.sgmu.seem.utils.enums.FolderTitle.INFOBLOCK_IMAGES;
import static ru.sgmu.seem.utils.enums.FolderTitle.MAN_IMAGES;
import static ru.sgmu.seem.utils.enums.PageTitle.MAIN;

@Controller("EditorMainController")
@RequestMapping(value = "/editor/main")
public class MainController {

    private Path mainFragmentPath = Paths.get("editor", "fragments", "main");

    private final CrudService<Man> manService;
    private final CrudService<Infoblock> infoblockService;
    private final FormValidator formValidator;
    private final ImageManager imageManager;

    @Autowired
    public MainController(CrudService<Man> manService,
                          CrudService<Infoblock> infoblockService,
                          FormValidator formValidator,
                          ImageManager imageManager) {
        this.manService = manService;
        this.infoblockService = infoblockService;
        this.formValidator = formValidator;
        this.imageManager = imageManager;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String main(Model model) {
        List<Man> manList = manService.getAll();
        List<Infoblock> infoblockList = infoblockService.getAll();
        manList.sort((e1, e2) -> (Long.compare(e1.getId(), e2.getId())));
        infoblockList.sort((e1, e2) -> (Long.compare(e1.getId(), e2.getId())));
        model.addAttribute("man_list", manList);
        model.addAttribute("infoblock_list", infoblockList);
        model.addAttribute(CONTENT.toString(), mainFragmentPath);
        model.addAttribute(MENU_OPTION.toString(), MenuOption.MAIN.name());
        model.addAttribute(TITLE.toString(), MAIN.getText());
        model.addAttribute(MAN_IMAGES.getText(), MAN_IMAGES_URL);
        model.addAttribute(INFOBLOCK_IMAGES.getText(), INFOBLOCK_IMAGES_URL);
        return "editor/layouts/index";
    }

    @RequestMapping(value = "/man/{id}", method = RequestMethod.POST)
    public String main(@Valid @ModelAttribute("man") Man man,
                       BindingResult bindingResult,
                       @PathVariable("id") Long formId,
                       @RequestParam("image") MultipartFile file,
                       RedirectAttributes redirectAttributes) throws IOException {
        Map<String, String> errors = FormValidator.getMap(bindingResult);
        errors.remove("imageName_error");
        String oldImageName = manService.getById(man.getId()).getImageName();
        String newImageExtention = file.getContentType().split("/")[1];
        String imageName = formValidator.checkImage(oldImageName, file);
        if (!file.isEmpty() && !imageManager.checkExtention(newImageExtention)) {
            errors.put(IMAGE_ERROR, IMAGE_WRONG_FORMAT);
        }
        man.setImageName(imageName);
        if (errors.size() != 0) {
            for (Map.Entry<String, String> entry : errors.entrySet()) {
                redirectAttributes.addFlashAttribute(entry.getKey().split("_")[0] + formId + "_error", entry.getValue());
            }
        } else {
            man.setDate(Date.valueOf(DateManager.getCurrentDate()));
            man.setTime(Time.valueOf(DateManager.getCurrentTime()));
            manService.update(man);
            imageManager.createImage(file, MAN_IMAGES_URL, imageName);
        }
        return "redirect:/editor/main";
    }

    @RequestMapping(value = "/infoblock/{id}", method = RequestMethod.POST)
    public String main(@Valid @ModelAttribute("infoblock") Infoblock infoblock,
                       BindingResult bindingResult,
                       @PathVariable("id") Long formId,
                       @RequestParam("image") MultipartFile file,
                       RedirectAttributes redirectAttributes) throws IOException {
        Map<String, String> errors = FormValidator.getMap(bindingResult);
        errors.remove("imageName_error");
        String oldImageName = infoblockService.getById(infoblock.getId()).getImageName();
        String newImageExtention = file.getContentType().split("/")[1];
        String imageName = formValidator.checkImage(oldImageName, file);
        if (!file.isEmpty() && !imageManager.checkExtention(newImageExtention)) {
            errors.put(IMAGE_ERROR, IMAGE_WRONG_FORMAT);
        }
        infoblock.setImageName(imageName);
        if (errors.size() != 0) {
            for (Map.Entry<String, String> entry : errors.entrySet()) {
                redirectAttributes.addFlashAttribute(entry.getKey().split("_")[0] + formId + "_ierror", entry.getValue());
            }
        } else {
            infoblock.setDate(Date.valueOf(DateManager.getCurrentDate()));
            infoblock.setTime(Time.valueOf(DateManager.getCurrentTime()));
            infoblockService.update(infoblock);
            imageManager.createImage(file, INFOBLOCK_IMAGES_URL, imageName);
        }
        return "redirect:/editor/main#infoform" + formId;
    }
}
