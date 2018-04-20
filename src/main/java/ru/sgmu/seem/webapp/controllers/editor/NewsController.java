package ru.sgmu.seem.webapp.controllers.editor;

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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;
import ru.sgmu.seem.utils.*;
import ru.sgmu.seem.utils.enums.FolderTitle;
import ru.sgmu.seem.utils.enums.MenuOption;
import ru.sgmu.seem.webapp.domains.News;
import ru.sgmu.seem.webapp.domains.User;
import ru.sgmu.seem.webapp.services.CustomUserDetailsService;
import ru.sgmu.seem.webapp.services.PageableService;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;

import static ru.sgmu.seem.utils.DateManager.*;
import static ru.sgmu.seem.utils.FolderManager.NEWS_IMAGES_URL;
import static ru.sgmu.seem.utils.FolderManager.newsImagesPath;
import static ru.sgmu.seem.utils.FormValidator.IMAGE_UPLOAD_ERROR;
import static ru.sgmu.seem.utils.enums.FolderTitle.NEWS_IMAGES;
import static ru.sgmu.seem.utils.enums.ListTitle.NEWS_LIST;
import static ru.sgmu.seem.utils.enums.PageAttribute.*;

@PreAuthorize("hasAnyRole('ADMIN','MODER')")
@Controller("EditorNewsController")
@RequestMapping(value = "/editor/news")
public class NewsController {

    private CustomUserDetailsService userService;
    private PageableService<News> newsPageableService;
    private FormValidator formValidator;
    private ImageManager imageManager;
    private Path news = Paths.get("editor", "fragments", "news", "news");
    private Path addNews = Paths.get("editor", "fragments", "news", "add");
    private Path editNews = Paths.get("editor", "fragments", "news", "edit");

    @Autowired
    public NewsController(@Qualifier("newsServiceImpl") PageableService<News> newsPageableService,
                          FormValidator formValidator,
                          ImageManager imageManager,
                          CustomUserDetailsService userService) {
        this.newsPageableService = newsPageableService;
        this.formValidator = formValidator;
        this.imageManager = imageManager;
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String news(Model model, Pageable pageable) {
        Page<News> newsPage = newsPageableService.getPage(pageable.getPageNumber(), 10,
                Sort.Direction.DESC, "date", "time");
        PageWrapper<News> page = new PageWrapper<>(newsPage, "news", 5);
        model.addAttribute(NEWS_LIST.name(), page.getContent())
                .addAttribute("page", page)
                .addAttribute(CONTENT.name(), news)
                .addAttribute(MENU_OPTION.name(), MenuOption.NEWS.name())
                .addAttribute(NEWS_IMAGES.getText(), NEWS_IMAGES_URL);
        return "editor/layouts/index";

    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String newsAdd(Model model,
                          News news) {
        model.addAttribute(CONTENT.name(), addNews)
                .addAttribute(MENU_OPTION.name(), MenuOption.NEWS.toString())
                .addAttribute(NEWS_IMAGES.getText(), NEWS_IMAGES_URL)
                .addAttribute("lang", LocaleContextHolder.getLocale().toString().split("-")[0]);
        return "editor/layouts/index";
    }

    @RequestMapping(value = "/add", params = "submit", method = RequestMethod.POST)
    public String newsAddSubmit(@Valid News news,
                                BindingResult bindingResult,
                                @RequestParam("image") MultipartFile file,
                                Model model,
                                Principal principal) {
        if (!formValidator.checkImage(file, bindingResult, model)) {
            model.addAttribute(CONTENT.name(), addNews)
                    .addAttribute(MENU_OPTION.toString(), MenuOption.NEWS.name());
            return "editor/layouts/index";
        }
        String imageName = imageManager.getImageName(file, newsImagesPath);
        User user = userService.getByUsername(principal.getName());
        news.setUser(user);
        news.setImageName(imageName);
        news.setDate(getCurrentDate());
        news.setTime(getCurrentTime());
        newsPageableService.add(news);
        return "redirect:/editor/news";
    }

    @RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
    public String newsEdit(Model model,
                           @PathVariable("id") Long id,
                           News news) {
        news = newsPageableService.getById(id);
        model.addAttribute(CONTENT.toString(), editNews)
                .addAttribute(NEWS_IMAGES.getText(), NEWS_IMAGES_URL)
                .addAttribute(MENU_OPTION.name(), MenuOption.NEWS.toString())
                .addAttribute("news", news)
                .addAttribute("lang", LocaleContextHolder.getLocale().toString().split("-")[0]);
        return "editor/layouts/index";
    }

    @RequestMapping(value = "/edit", params = "submit", method = RequestMethod.POST)
    public String newsEditSubmit(@Valid News news,
                                 BindingResult bindingResult,
                                 @RequestParam("image") MultipartFile file,
                                 Model model,
                                 Principal principal) {
        String oldImageName = newsPageableService.getById(news.getId()).getImageName();
        if (!imageManager.checkExtention(file) && !file.isEmpty()){
            model.addAttribute("image_err", IMAGE_UPLOAD_ERROR);
        }
        if (bindingResult.hasErrors() || model.containsAttribute("image_err")) {
            news.setImageName(oldImageName);
            model.addAttribute(CONTENT.name(), editNews)
                    .addAttribute(MENU_OPTION.name(), MenuOption.NEWS.toString())
                    .addAttribute("news", news)
                    .addAttribute(FolderTitle.NEWS_IMAGES.getText(), NEWS_IMAGES_URL);
            return "editor/layouts/index";
        }
        String imageName = (!file.isEmpty()) ? imageManager.getImageName(file, newsImagesPath) : oldImageName;
        if (!imageName.equals(oldImageName)){
            try {
                imageManager.deleteImage(Paths.get(newsImagesPath.toString(), oldImageName));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        User user = userService.getByUsername(principal.getName());
        news.setUser(user);
        news.setImageName(imageName);
        news.setDate(getCurrentDate());
        news.setTime(getCurrentTime());
        newsPageableService.update(news);
        return "redirect:/editor/news";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public RedirectView newsDelete(@RequestParam("id") Long id) {
        newsPageableService.remove(id);
        return new RedirectView("/editor/news", true);
    }
}
