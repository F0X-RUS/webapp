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
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import static ru.sgmu.seem.utils.DateManager.*;
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

    private Path newsListFragmentPath = Paths.get("fragments", "news", "news");
    private Path newsItemFragmentPath = Paths.get("fragments", "news", "item");

    @Autowired
    public NewsController(NewsServiceImpl newsService) {
        this.newsService = newsService;
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

}
