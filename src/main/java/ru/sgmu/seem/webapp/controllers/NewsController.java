package ru.sgmu.seem.webapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.sgmu.seem.utils.PageWrapper;
import ru.sgmu.seem.utils.enums.MenuOption;
import ru.sgmu.seem.webapp.domains.News;
import ru.sgmu.seem.webapp.services.NewsServiceImpl;

import java.nio.file.Path;
import java.nio.file.Paths;

import static ru.sgmu.seem.utils.FolderManager.NEWS_IMAGES_URL;
import static ru.sgmu.seem.utils.enums.FolderTitle.NEWS_IMAGES;
import static ru.sgmu.seem.utils.enums.ListTitle.NEWS_LIST;
import static ru.sgmu.seem.utils.enums.PageAttribute.CONTENT;
import static ru.sgmu.seem.utils.enums.PageAttribute.MENU_OPTION;

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
    public String getList(Model model,
                          Pageable pageable) {
        Page<News> newsPage = newsService.getPage(pageable.getPageNumber(), 9,
                Sort.Direction.DESC, "date", "time");
        PageWrapper<News> page = new PageWrapper<>(newsPage, "news", 5);
        model.addAttribute(NEWS_LIST.name(), page.getContent())
                .addAttribute("page", page)
                .addAttribute(NEWS_IMAGES.getText(), NEWS_IMAGES_URL)
                .addAttribute(CONTENT.name(), newsListFragmentPath)
                .addAttribute(MENU_OPTION.name(), MenuOption.NEWS.toString());
        return "layouts/main";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String getOne(@PathVariable("id") Long id,
                         Model model) {
        News news = newsService.getById(id);
        model.addAttribute(NEWS_IMAGES.getText(), NEWS_IMAGES_URL)
                .addAttribute("news", news)
                .addAttribute(CONTENT.toString(), newsItemFragmentPath)
                .addAttribute(MENU_OPTION.toString(), MenuOption.NEWS.toString());
        return "layouts/main";
    }

}
