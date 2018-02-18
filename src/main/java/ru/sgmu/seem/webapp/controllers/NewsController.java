package ru.sgmu.seem.webapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.sgmu.seem.utils.*;
import ru.sgmu.seem.utils.enums.MenuOption;
import ru.sgmu.seem.webapp.domains.News;
import ru.sgmu.seem.webapp.services.NewsServiceImpl;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import javax.validation.Valid;
import java.io.IOException;

import static ru.sgmu.seem.utils.FolderManager.*;
import static ru.sgmu.seem.utils.FormValidator.IMAGE_CHOOSE_FILE;
import static ru.sgmu.seem.utils.FormValidator.IMAGE_WRONG_FORMAT;
import static ru.sgmu.seem.utils.ImageManager.*;
import static ru.sgmu.seem.utils.enums.PageAttribute.*;
import static ru.sgmu.seem.utils.enums.FolderTitle.*;
import static ru.sgmu.seem.utils.enums.ListTitle.*;
import static ru.sgmu.seem.utils.enums.PageTitle.*;

@Controller
@RequestMapping(value = "/news")
public class NewsController {

    private final NewsServiceImpl newsService;
    private final ImageManager imageManager;

    private Path newsListFragmentPath = Paths.get("fragments", "news", "news");
    private Path newsItemFragmentPath = Paths.get("fragments", "news", "item");
    private Path newsAddFragmentPath = Paths.get("fragments", "news", "add");
    private Path newsEditFragmentPath = Paths.get("fragments", "news", "edit");
    private Path newsDeleteFragmentPath = Paths.get("fragments", "news", "delete");

    @Autowired
    public NewsController(NewsServiceImpl newsService,
                          ImageManager imageManager) {
        this.newsService = newsService;
        this.imageManager = imageManager;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String getList(Model model, Pageable pageable) {
        Page<News> newsPage = newsService.getPage(pageable.getPageNumber(), 9,
                Sort.Direction.DESC, "date", "time");
        PageWrapper<News> page = new PageWrapper<>(newsPage, "news", 5);
        model.addAttribute(NEWS_LIST.name(), page.getContent())
                .addAttribute("page", page)
                .addAttribute(NEWS_IMAGES.getText(), NEWS_IMAGES_URL)
                .addAttribute(CONTENT.name(), newsListFragmentPath)
                .addAttribute(TITLE.name(), NEWS.getText())
                .addAttribute(CURRENT_PAGE.name(), MenuOption.NEWS.toString());
        return "layouts/main";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String getOne(@PathVariable("id") Long id,
                         Model model) {
        News news = newsService.getById(id);
        model.addAttribute(NEWS_IMAGES.getText(), NEWS_IMAGES_URL)
                .addAttribute("news", news)
                .addAttribute(CONTENT.toString(), newsItemFragmentPath)
                .addAttribute(TITLE.toString(), NEWS.getText() + " - " + news.getTitle())
                .addAttribute(CURRENT_PAGE.toString(), MenuOption.NEWS.toString());
        return "layouts/main";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String newsAdd(Model model,
                          News news) {
        model.addAttribute(CONTENT.name(), newsAddFragmentPath)
                .addAttribute(TITLE.name(), ADD_NEWS.getText())
                .addAttribute(CURRENT_PAGE.name(), MenuOption.NEWS.toString())
                .addAttribute(NEWS_IMAGES.getText(), NEWS_IMAGES_URL);
        return "layouts/main";
    }

    @RequestMapping(value = "/add", params = "submit", method = RequestMethod.POST)
    public String newsAddSubmit(@Valid News news,
                                BindingResult bindingResult,
                                @RequestParam("image") MultipartFile file,
                                Model model) {
        if (file.isEmpty()) {
            model.addAttribute("image_err", IMAGE_CHOOSE_FILE);
        }
        if (!imageManager.checkExtention(file.getContentType().split("/")[1].toUpperCase())) {
            model.addAttribute("image_err", IMAGE_WRONG_FORMAT);
        }
        if (bindingResult.hasErrors() || model.containsAttribute("image_err")) {
            model.addAttribute(CONTENT.name(), newsAddFragmentPath)
                    .addAttribute(TITLE.name(), ADD_NEWS.getText())
                    .addAttribute(CURRENT_PAGE.name(), MenuOption.NEWS.toString())
                    .addAttribute(NEWS_IMAGES.getText(), NEWS_IMAGES_URL);
            return "layouts/main";
        }
        String imageName = null;
        try {
            imageName = imageManager.parseFile(file);
            imageManager.createImage(file, FolderManager.newsImagesPath.toString(), imageName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        news.setImageName(imageName);
        news.setDate(Date.valueOf(DateManager.getCurrentDate()));
        news.setTime(Time.valueOf(DateManager.getCurrentTime()));
        newsService.add(news);
        return "redirect:/news";
    }

    @RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
    public String newsEdit(Model model,
                           @PathVariable("id") Long id,
                           News news) {
        news = newsService.getById(id);
        model.addAttribute(CONTENT.toString(), newsEditFragmentPath)
                .addAttribute(TITLE.toString(), EDIT_NEWS.getText())
                .addAttribute(NEWS_IMAGES.getText(), NEWS_IMAGES_URL)
                .addAttribute(CURRENT_PAGE.name(), MenuOption.NEWS.toString())
                .addAttribute("news", news);
        return "layouts/main";
    }

    @RequestMapping(value = "/edit", params = "submit", method = RequestMethod.POST)
    public String newsEditSubmit(@Valid News news,
                                BindingResult bindingResult,
                                @RequestParam("image") MultipartFile file,
                                Model model) {
        if (!file.isEmpty() && !imageManager.checkExtention(file.getContentType().split("/")[1].toUpperCase())) {
            model.addAttribute("image_err", IMAGE_WRONG_FORMAT);
        }
        if (bindingResult.hasErrors() || model.containsAttribute("image_err")) {
            news.setImageName(newsService.getById(news.getId()).getImageName());
            model.addAttribute(CONTENT.name(), newsEditFragmentPath)
                    .addAttribute(TITLE.name(), EDIT_NEWS.getText())
                    .addAttribute(CURRENT_PAGE.name(), MenuOption.NEWS.toString())
                    .addAttribute("news", news)
                    .addAttribute(NEWS_IMAGES.getText(), NEWS_IMAGES_URL);
            return "layouts/main";
        }
        String imageName = null;
        if (!file.isEmpty()) {
            try {
                imageName = imageManager.parseFile(file);
                imageManager.createImage(file, FolderManager.newsImagesPath.toString(), imageName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            imageName = newsService.getById(news.getId()).getImageName();
        }
        news.setImageName(imageName);
        news.setDate(Date.valueOf(DateManager.getCurrentDate()));
        news.setTime(Time.valueOf(DateManager.getCurrentTime()));
        newsService.update(news);
        return "redirect:/news";
    }

}
