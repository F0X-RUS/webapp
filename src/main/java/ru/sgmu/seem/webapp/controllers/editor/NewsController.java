package ru.sgmu.seem.webapp.controllers.editor;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import ru.sgmu.seem.webapp.services.NewsService;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static ru.sgmu.seem.utils.DateManager.*;
import static ru.sgmu.seem.utils.FolderManager.NEWS_IMAGES_URL;
import static ru.sgmu.seem.utils.FolderManager.newsImagesPath;
import static ru.sgmu.seem.utils.FormValidator.IMAGE_CHOOSE_FILE;
import static ru.sgmu.seem.utils.FormValidator.IMAGE_UPLOAD_ERROR;
import static ru.sgmu.seem.utils.FormValidator.IMAGE_WRONG_FORMAT;
import static ru.sgmu.seem.utils.enums.FolderTitle.NEWS_IMAGES;
import static ru.sgmu.seem.utils.enums.ListTitle.NEWS_LIST;
import static ru.sgmu.seem.utils.enums.PageAttribute.*;
import static ru.sgmu.seem.utils.enums.PageAttribute.CURRENT_PAGE;
import static ru.sgmu.seem.utils.enums.PageTitle.*;

@Controller("EditorNewsController")
@RequestMapping(value = "/editor/news")
public class NewsController {

    private NewsService newsService;
    private FormValidator formValidator;
    private ImageManager imageManager;
    private Path news = Paths.get("editor", "fragments", "news", "news");
    private Path addNews = Paths.get("editor", "fragments", "news", "add");
    private Path editNews = Paths.get("editor", "fragments", "news", "edit");

    @Autowired
    public NewsController(NewsService newsService, FormValidator formValidator, ImageManager imageManager) {
        this.newsService = newsService;
        this.formValidator = formValidator;
        this.imageManager = imageManager;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String news(Model model, Pageable pageable) {
        Page<News> newsPage = newsService.getPage(pageable.getPageNumber(), 10,
                Sort.Direction.DESC, "date", "time");
        PageWrapper<News> page = new PageWrapper<>(newsPage, "news", 5);
        model.addAttribute(NEWS_LIST.name(), page.getContent())
                .addAttribute("page", page)
                .addAttribute(CONTENT.name(), news)
                .addAttribute(MENU_OPTION.name(), MenuOption.NEWS.name())
                .addAttribute(TITLE.name(), NEWS.getText())
                .addAttribute(NEWS_IMAGES.getText(), NEWS_IMAGES_URL);
        return "editor/layouts/index";

    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String newsAdd(Model model,
                          News news) {
        model.addAttribute(CONTENT.name(), addNews)
                .addAttribute(TITLE.name(), ADD_NEWS.getText())
                .addAttribute(CURRENT_PAGE.name(), MenuOption.NEWS.toString())
                .addAttribute(NEWS_IMAGES.getText(), NEWS_IMAGES_URL);
        return "editor/layouts/index";
    }

    @RequestMapping(value = "/add", params = "submit", method = RequestMethod.POST)
    public String newsAddSubmit(@Valid News news,
                                BindingResult bindingResult,
                                @RequestParam("image") MultipartFile file,
                                Model model) {
        if (!formValidator.check(file, bindingResult, model)) {
            model.addAttribute(CONTENT.name(), addNews)
                    .addAttribute(MENU_OPTION.toString(), MenuOption.NEWS.name())
                    .addAttribute(TITLE.name(), ADD_NEWS.getText());
            return "/editor/layouts/index";
        }
        String imageName = imageManager.getImageName(file, newsImagesPath);
        news.setImageName(imageName);
        news.setDate(getCurrentDate());
        news.setTime(getCurrentTime());
        newsService.add(news);
        return "redirect:/editor/news";
    }

    @RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
    public String newsEdit(Model model,
                           @PathVariable("id") Long id,
                           News news) {
        news = newsService.getById(id);
        model.addAttribute(CONTENT.toString(), editNews)
                .addAttribute(TITLE.toString(), EDIT_NEWS.getText())
                .addAttribute(NEWS_IMAGES.getText(), NEWS_IMAGES_URL)
                .addAttribute(CURRENT_PAGE.name(), MenuOption.NEWS.toString())
                .addAttribute("news", news);
        return "editor/layouts/index";
    }

    @RequestMapping(value = "/edit", params = "submit", method = RequestMethod.POST)
    public String newsEditSubmit(@Valid News news,
                                 BindingResult bindingResult,
                                 @RequestParam("image") MultipartFile file,
                                 Model model) {
        String oldImageName = newsService.getById(news.getId()).getImageName();
        String fileExtention = file.getContentType().split("/")[1].toUpperCase();
        if (!imageManager.checkExtention(fileExtention) && !file.isEmpty()){
            model.addAttribute("image_err", IMAGE_UPLOAD_ERROR);
        }
        if (bindingResult.hasErrors() || model.containsAttribute("image_err")) {
            news.setImageName(oldImageName);
            model.addAttribute(CONTENT.name(), editNews)
                    .addAttribute(TITLE.name(), EDIT_NEWS.getText())
                    .addAttribute(CURRENT_PAGE.name(), MenuOption.NEWS.toString())
                    .addAttribute("news", news)
                    .addAttribute(FolderTitle.NEWS_IMAGES.getText(), NEWS_IMAGES_URL);
            return "/editor/layouts/index";
        }
        String imageName = (!file.isEmpty()) ? imageManager.getImageName(file, newsImagesPath) : oldImageName;
        if (!imageName.equals(oldImageName)){
            try {
                imageManager.deleteImage(Paths.get(newsImagesPath.toString(), oldImageName));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        news.setImageName(imageName);
        news.setDate(getCurrentDate());
        news.setTime(getCurrentTime());
        newsService.update(news);
        return "redirect:/editor/news";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public RedirectView newsDelete(@RequestParam("id") Long id) {
        newsService.remove(id);
        return new RedirectView("/editor/news", true);
    }
}
