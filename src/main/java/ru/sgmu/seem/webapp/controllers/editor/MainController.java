package ru.sgmu.seem.webapp.controllers.editor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.sgmu.seem.utils.FormValidator;
import ru.sgmu.seem.utils.ImageManager;
import ru.sgmu.seem.utils.enums.FolderTitle;
import ru.sgmu.seem.utils.enums.MenuOption;
import ru.sgmu.seem.webapp.domains.Infoblock;
import ru.sgmu.seem.webapp.domains.Man;
import ru.sgmu.seem.webapp.services.CrudService;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
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
import static ru.sgmu.seem.utils.enums.PageTitle.EDIT_INFOBLOCK;
import static ru.sgmu.seem.utils.enums.PageTitle.EDIT_MAN;
import static ru.sgmu.seem.utils.enums.PageTitle.MAIN;

@Controller("EditorMainController")
@RequestMapping(value = "/editor/main")
public class MainController {

    private Path main = Paths.get("editor", "fragments", "main", "main");
    private Path editMan = Paths.get("editor", "fragments", "main", "edit-man");
    private Path editInfoblock = Paths.get("editor", "fragments", "main", "edit-infoblock");
    private CrudService<Man> manService;
    private CrudService<Infoblock> infoblockService;
    private FormValidator formValidator;
    private ImageManager imageManager;

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
        model.addAttribute(MAN_LIST.name(), manList)
                .addAttribute(INFOBLOCK_LIST.name(), infoblockList)
                .addAttribute(CONTENT.toString(), main)
                .addAttribute(MENU_OPTION.toString(), MenuOption.MAIN.name())
                .addAttribute(TITLE.toString(), MAIN.getText())
                .addAttribute(MAN_IMAGES.getText(), MAN_IMAGES_URL)
                .addAttribute(INFOBLOCK_IMAGES.getText(), INFOBLOCK_IMAGES_URL);
        return "editor/layouts/index";
    }

    @RequestMapping(value = "/man/{id}", method = RequestMethod.GET)
    public String main(Man man,
                       @PathVariable("id") Long id,
                       Model model) {
        man = manService.getById(id);
        model.addAttribute(CONTENT.toString(), editMan)
                .addAttribute(TITLE.toString(), EDIT_MAN.getText())
                .addAttribute(MAN_IMAGES.getText(), MAN_IMAGES_URL)
                .addAttribute(MENU_OPTION.toString(), MenuOption.MAIN.name())
                .addAttribute("man", man);
        return "editor/layouts/index";
    }

    @RequestMapping(value = "/man/save", method = RequestMethod.POST)
    public String main(@Valid Man man,
                       BindingResult bindingResult,
                       @RequestParam("image") MultipartFile file,
                       Model model) {
        String oldImageName = manService.getById(man.getId()).getImageName();
        String fileExtention = file.getContentType().split("/")[1].toUpperCase();
        if (!imageManager.checkExtention(fileExtention) && !file.isEmpty()){
            model.addAttribute("image_err", IMAGE_UPLOAD_ERROR);
        }
        if (bindingResult.hasErrors() || model.containsAttribute("image_err")) {
            man.setImageName(oldImageName);
            model.addAttribute(CONTENT.name(), editMan)
                    .addAttribute(TITLE.name(), EDIT_MAN.getText())
                    .addAttribute(CURRENT_PAGE.name(), MenuOption.MAIN.toString())
                    .addAttribute("man", man)
                    .addAttribute(FolderTitle.MAN_IMAGES.getText(), MAN_IMAGES_URL);
            return "/editor/layouts/index";
        }
        String imageName = (!file.isEmpty()) ? imageManager.getImageName(file, manImagesPath) : oldImageName;
        if (!imageName.equals(oldImageName)){
            try {
                imageManager.deleteImage(Paths.get(manImagesPath.toString(), oldImageName));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        man.setImageName(imageName);
        man.setDate(getCurrentDate());
        man.setTime(getCurrentTime());
        manService.update(man);
        return "redirect:/editor/main";
    }

    @RequestMapping(value = "/infoblock/{id}", method = RequestMethod.GET)
    public String main(Infoblock infoblock,
                       @PathVariable("id") Long id,
                       Model model) {
        infoblock = infoblockService.getById(id);
        model.addAttribute(CONTENT.toString(), editInfoblock)
                .addAttribute(TITLE.toString(), EDIT_INFOBLOCK.getText())
                .addAttribute(INFOBLOCK_IMAGES.getText(), INFOBLOCK_IMAGES_URL)
                .addAttribute(MENU_OPTION.toString(), MenuOption.MAIN.name())
                .addAttribute("infoblock", infoblock);
        return "editor/layouts/index";
    }

    @RequestMapping(value = "/infoblock/save", method = RequestMethod.POST)
    public String main(@Valid Infoblock infoblock,
                       BindingResult bindingResult,
                       @RequestParam("image") MultipartFile file,
                       Model model) {
        String oldImageName = infoblockService.getById(infoblock.getId()).getImageName();
        String fileExtention = file.getContentType().split("/")[1].toUpperCase();
        if (!imageManager.checkExtention(fileExtention) && !file.isEmpty()){
            model.addAttribute("image_err", IMAGE_UPLOAD_ERROR);
        }
        if (bindingResult.hasErrors() || model.containsAttribute("image_err")) {
            infoblock.setImageName(oldImageName);
            model.addAttribute(CONTENT.name(), editInfoblock)
                    .addAttribute(TITLE.name(), EDIT_INFOBLOCK.getText())
                    .addAttribute(CURRENT_PAGE.name(), MenuOption.MAIN.toString())
                    .addAttribute("infoblock", infoblock)
                    .addAttribute(FolderTitle.INFOBLOCK_IMAGES.getText(), INFOBLOCK_IMAGES_URL);
            return "/editor/layouts/index";
        }
        String imageName = (!file.isEmpty()) ? imageManager.getImageName(file, infoblockImagesPath) : oldImageName;
        if (!imageName.equals(oldImageName)){
            try {
                imageManager.deleteImage(Paths.get(infoblockImagesPath.toString(), oldImageName));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        infoblock.setImageName(imageName);
        infoblock.setDate(getCurrentDate());
        infoblock.setTime(getCurrentTime());
        infoblockService.update(infoblock);
        return "redirect:/editor/main";
    }
}
