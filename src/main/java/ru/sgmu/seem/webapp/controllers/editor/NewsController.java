package ru.sgmu.seem.webapp.controllers.editor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import ru.sgmu.seem.utils.*;
import ru.sgmu.seem.utils.enums.MenuOption;
import ru.sgmu.seem.webapp.domains.News;
import ru.sgmu.seem.webapp.services.NewsService;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.sql.Time;
import java.util.Map;

import static ru.sgmu.seem.utils.FolderManager.NEWS_IMAGES_URL;
import static ru.sgmu.seem.utils.FormValidator.IMAGE_CHOOSE_FILE;
import static ru.sgmu.seem.utils.FormValidator.IMAGE_ERROR;
import static ru.sgmu.seem.utils.FormValidator.IMAGE_WRONG_FORMAT;
import static ru.sgmu.seem.utils.enums.PageAttribute.CONTENT;
import static ru.sgmu.seem.utils.enums.PageAttribute.MENU_OPTION;
import static ru.sgmu.seem.utils.enums.PageAttribute.TITLE;
import static ru.sgmu.seem.utils.enums.FolderTitle.NEWS_IMAGES;
import static ru.sgmu.seem.utils.enums.ListTitle.NEWS_LIST;
import static ru.sgmu.seem.utils.enums.PageTitle.*;

@Controller("EditorNewsController")
@RequestMapping(value = "/editor/news")
public class NewsController {

    private final NewsService newsService;
    private final ImageManager imageManager;
    private final FilenameGenerator filenameGenerator;

    private Path newsFragmentPath = Paths.get("editor", "fragments", "news", "news");
    private Path addNewsFragmentPath = Paths.get("editor", "fragments", "news", "add");
    private Path editNewsFragmentPath = Paths.get("editor", "fragments", "news", "edit");
    private Path deleteNewsFragmentPath = Paths.get("editor", "fragments", "news", "delete");

    @Autowired
    public NewsController(NewsService newsService,
                          FilenameGenerator filenameGenerator,
                          ImageManager imageManager){
        this.newsService = newsService;
        this.filenameGenerator = filenameGenerator;
        this.imageManager = imageManager;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String news(Model model, Pageable pageable) {
        Page<News> newsPage = newsService.getPage(pageable.getPageNumber(), 10,
                Sort.Direction.DESC, "date", "time");
        PageWrapper<News> page = new PageWrapper<>(newsPage, "news", 5);
        model.addAttribute(NEWS_LIST.name(), page.getContent())
                .addAttribute("page", page)
                .addAttribute(CONTENT.name(), newsFragmentPath)
                .addAttribute(MENU_OPTION.name(), MenuOption.NEWS.name())
                .addAttribute(TITLE.name(), NEWS.getText())
                .addAttribute(NEWS_IMAGES.getText(), NEWS_IMAGES_URL);
        return "editor/layouts/index";

    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String newsAdd(Model model) {
        model.addAttribute(MENU_OPTION.name(), MenuOption.NEWS.name())
                .addAttribute(TITLE.name(), ADD_NEWS.getText())
                .addAttribute(NEWS_IMAGES.getText(), NEWS_IMAGES_URL)
                .addAttribute(CONTENT.name(), addNewsFragmentPath);
        return "editor/layouts/index";
    }

    @RequestMapping(value = "/add", params = "submit", method = RequestMethod.POST)
    public RedirectView newsAddSubmit(@Valid @ModelAttribute("news") News news,
                                      BindingResult bindingResult,
                                      @RequestParam("image") MultipartFile file,
                                      RedirectAttributes redirectAttributes) {
        try {
            Map<String, String> errors = FormValidator.getMap(bindingResult);
            if (!file.isEmpty()) {
                String extention = file.getContentType().split("/")[1];
                String name = filenameGenerator.nextString(50);
                String imageName = name + "." + extention;
                if (!imageManager.checkExtention(extention)) {
                    throw new IOException();
                }
                imageManager.createImage(file, NEWS_IMAGES_URL, imageName);
                news.setImageName(imageName);
                news.setDate(Date.valueOf(DateManager.getCurrentDate()));
                news.setTime(Time.valueOf(DateManager.getCurrentTime()));
            } else {
                errors.put(IMAGE_ERROR, IMAGE_CHOOSE_FILE);
            }
            if (bindingResult.hasErrors() || errors.size() != 0) {
                for (Map.Entry<String, String> entry : errors.entrySet()) {
                    redirectAttributes.addFlashAttribute(entry.getKey(), entry.getValue());
                }
                return new RedirectView("/editor/news/add", true);
            } else {
                newsService.add(news);
                return new RedirectView("/editor/news", true);
            }
        } catch (IOException ioe) {
            redirectAttributes.addFlashAttribute("message", IMAGE_WRONG_FORMAT + file.getOriginalFilename());
            return new RedirectView("/editor/news/add", true);
        }
    }

    @RequestMapping(value = "/add", params = "cancel", method = RequestMethod.POST)
    public RedirectView newsAddCancel() {
        return new RedirectView("/editor/news", true);
    }

    @RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
    public String newsEdit(Model model,
                           @PathVariable("id") Long id) {
        News news = newsService.getById(id);
        model.addAttribute(CONTENT.toString(), editNewsFragmentPath)
                .addAttribute(MENU_OPTION.toString(), MenuOption.NEWS.name())
                .addAttribute(TITLE.toString(), EDIT_NEWS.getText())
                .addAttribute(NEWS_IMAGES.getText(), NEWS_IMAGES_URL)
                .addAttribute("news", news);
        return "editor/layouts/index";
    }

    @RequestMapping(value = "/edit", params = "submit", method = RequestMethod.POST)
    public RedirectView newsEdit(@Valid @ModelAttribute("news") News news,
                                 BindingResult bindingResult,
                                 @RequestParam("id") Long id,
                                 @RequestParam("image") MultipartFile file,
                                 RedirectAttributes redirectAttributes) {
        try {
            if (bindingResult.hasErrors()) {
                Map<String, String> errors = FormValidator.getMap(bindingResult);
                for (Map.Entry<String, String> entry : errors.entrySet()) {
                    redirectAttributes.addFlashAttribute(entry.getKey(), entry.getValue());
                }
                return new RedirectView("/editor/news/" + id + "/edit", true);
            }
            news.setDate(Date.valueOf(DateManager.getCurrentDate()));
            news.setTime(Time.valueOf(DateManager.getCurrentTime()));
            if (!file.isEmpty()) {
                String extention = file.getContentType().split("/")[1];
                String name = filenameGenerator.nextString(50);
                String imageName = name + "." + extention;
                if (!imageManager.checkExtention(extention)) {
                    throw new IOException();
                }
                imageManager.createImage(file, NEWS_IMAGES_URL, imageName);
                Path oldImage = Paths.get(NEWS_IMAGES_URL, newsService.getById(news.getId()).getImageName());
                if (Files.exists(oldImage)) {
                    imageManager.deleteImage(oldImage);
                }
                news.setImageName(imageName);
            } else {
                news.setImageName(newsService.getById(id).getImageName());
            }
            try {
                newsService.update(news);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return new RedirectView("/editor/news", true);
        } catch (IOException ioe) {
            redirectAttributes.addFlashAttribute("message", "Ошибка загрузки: " + file.getOriginalFilename());
            return new RedirectView("/editor/news/" + id + "/edit", true);
        }
    }

    @RequestMapping(value = "/edit", params = "cancel", method = RequestMethod.POST)
    public RedirectView newsEdit() {
        return new RedirectView("/editor/news", true);
    }

    @RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
    public String newsDelete(Model model,
                             @PathVariable("id") Long id) {
        News news = newsService.getById(id);
        model.addAttribute("news", news);
        model.addAttribute(CONTENT.toString(), deleteNewsFragmentPath);
        model.addAttribute(MENU_OPTION.toString(), MenuOption.NEWS.name());
        model.addAttribute(TITLE.toString(), DELETE_NEWS.getText());
        model.addAttribute(NEWS_IMAGES.getText(), NEWS_IMAGES_URL);
        return "editor/layouts/index";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public RedirectView newsDelete(@RequestParam("id") Long id) {
        newsService.remove(id);
        return new RedirectView("/editor/news", true);
    }
}
