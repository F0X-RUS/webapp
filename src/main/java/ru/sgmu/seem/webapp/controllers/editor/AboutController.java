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
import ru.sgmu.seem.webapp.domains.Passage;
import ru.sgmu.seem.webapp.domains.User;
import ru.sgmu.seem.webapp.services.CustomUserDetailsService;
import ru.sgmu.seem.webapp.services.PassageService;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.Comparator;
import java.util.List;

import static ru.sgmu.seem.utils.DateManager.getCurrentDate;
import static ru.sgmu.seem.utils.DateManager.getCurrentTime;
import static ru.sgmu.seem.utils.FolderManager.PASSAGE_IMAGES_URL;
import static ru.sgmu.seem.utils.FolderManager.passageImagesPath;
import static ru.sgmu.seem.utils.FormValidator.IMAGE_UPLOAD_ERROR;
import static ru.sgmu.seem.utils.enums.FolderTitle.PASSAGE_IMAGES;
import static ru.sgmu.seem.utils.enums.MenuOption.ABOUT;
import static ru.sgmu.seem.utils.enums.PageAttribute.*;
import static ru.sgmu.seem.utils.enums.PageAttribute.MENU_OPTION;

@PreAuthorize("hasRole('ADMIN')")
@Controller("EditorAboutController")
@RequestMapping(value = "/editor/about")
public class AboutController {

    private Path about = Paths.get("editor", "fragments", "about", "about");
    private Path editPassage = Paths.get("editor", "fragments", "about", "edit");

    private ImageManager imageManager;
    private FormValidator formValidator;
    private final PassageService passageService;
    private CustomUserDetailsService userService;

    @Autowired
    public AboutController(PassageService passageService,
                           CustomUserDetailsService userService,
                           FormValidator formValidator,
                           ImageManager imageManager) {
        this.passageService = passageService;
        this.userService = userService;
        this.formValidator = formValidator;
        this.imageManager = imageManager;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String about(Model model) {
        List<Passage> list = passageService.getAll();
        list.sort(Comparator.comparing(EntityDetails::getDate).thenComparing(EntityDetails::getTime).reversed());
        model.addAttribute(CONTENT.name(), about)
                .addAttribute(MENU_OPTION.name(), ABOUT.name())
                .addAttribute(ListTitle.PASSAGE_LIST.name(), list)
                .addAttribute(PASSAGE_IMAGES.name(), PASSAGE_IMAGES_URL);
        return "editor/layouts/index";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(Model model) {
        if (!model.containsAttribute("passage")) {
            model.addAttribute("passage", new Passage());
        }
        model.addAttribute(CONTENT.name(), editPassage)
                .addAttribute(MENU_OPTION.name(), MenuOption.CONTACTS.name())
                .addAttribute(PASSAGE_IMAGES.name(), PASSAGE_IMAGES_URL)
                .addAttribute("lang", LocaleContextHolder.getLocale().toString().split("-")[0]);
        return "editor/layouts/index";
    }

    @RequestMapping(value = "/save", params = "submit", method = RequestMethod.POST)
    public String EditSubmit(@Valid Passage passage,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes,
                             @RequestParam("image") MultipartFile file,
                             Principal principal,
                             Model model) {
        if (!file.isEmpty() || bindingResult.hasErrors()) {
            if (!formValidator.checkImage(file, bindingResult, model)) {
                if (!imageManager.checkSelectedImage(file)) {
                    redirectAttributes.addFlashAttribute("image_err", IMAGE_UPLOAD_ERROR);
                }
                redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.passage", bindingResult);
                redirectAttributes.addFlashAttribute("passage", passage);
                return "redirect:/editor/about/add";
            }
            String imageName = imageManager.getImageName(file, passageImagesPath);
            passage.setImageName(imageName);
        }
        User user = userService.getByUsername(principal.getName());
        passage.setUser(user);
        passage.setDate(getCurrentDate());
        passage.setTime(getCurrentTime());
        passageService.update(passage);
        return "redirect:/editor/about";
    }

    @RequestMapping(value = "{id}/save", method = RequestMethod.POST)
    public String add(@Valid Passage passage,
                      BindingResult bindingResult,
                      RedirectAttributes redirectAttributes,
                      Principal principal,
                      @Param("clear") boolean clear,
                      @RequestParam("image") MultipartFile file) throws IOException {
        String oldImageName = passageService.getById(passage.getId()).getImageName();
        String imageName = oldImageName;
        if (!file.isEmpty()) {
            if (!imageManager.checkExtention(file)) {
                redirectAttributes.addFlashAttribute("image_err", IMAGE_UPLOAD_ERROR);
            } else {
                imageName = imageManager.getImageName(file, passageImagesPath);
            }
        }
        if (bindingResult.hasErrors() || !redirectAttributes.getFlashAttributes().isEmpty()) {
            passage.setImageName(oldImageName);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.passage", bindingResult);
            redirectAttributes.addFlashAttribute("passage", passage);
            return "redirect:/editor/about/" + passage.getId() + "/edit";
        }
        if (imageName != null && oldImageName != null && !imageName.equals(oldImageName)) {
            imageManager.deleteImage(Paths.get(passageImagesPath.toString(), oldImageName));
        }
        if (clear) {
            imageName = null;
            imageManager.deleteImage(Paths.get(passageImagesPath.toString(), oldImageName));
        }
        passage.setImageName(imageName);
        User user = userService.getByUsername(principal.getName());
        passage.setUser(user);
        passage.setDate(getCurrentDate());
        passage.setTime(getCurrentTime());
        passageService.add(passage);
        return "redirect:/editor/about";
    }

    @RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
    public String Edit(Model model,
                       @PathVariable("id") Long id) {
        Passage passage = passageService.getById(id);
        if (!model.containsAttribute("passage")) {
            model.addAttribute("passage", passage);
        }
        model.addAttribute(CONTENT.toString(), editPassage)
                .addAttribute(MENU_OPTION.name(), MenuOption.ABOUT.toString())
                .addAttribute(PASSAGE_IMAGES.name(), PASSAGE_IMAGES_URL)
                .addAttribute("lang", LocaleContextHolder.getLocale().toString().split("-")[0]);
        return "editor/layouts/index";
    }


    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public RedirectView Delete(@RequestParam("id") Long id) {
        passageService.remove(id);
        return new RedirectView("/editor/about", true);
    }
}
