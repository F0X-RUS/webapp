package ru.sgmu.seem.webapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.sgmu.seem.utils.DateManager;
import ru.sgmu.seem.utils.FilenameGenerator;
import ru.sgmu.seem.utils.FolderManager;
import ru.sgmu.seem.utils.ImageManager;
import ru.sgmu.seem.webapp.domains.News;
import ru.sgmu.seem.webapp.services.NewsServiceImpl;

import java.sql.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping(value = "/news")
public class NewsController {

    @Autowired
    private NewsServiceImpl newsService;

    @RequestMapping(method = RequestMethod.GET)
    public String getList(Model model, HttpServletResponse response){
        model.addAttribute("news_list", newsService.getAll());
        model.addAttribute("news_images_dir", FolderManager.NEWS_IMAGES_URL);
        return "news/news";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(Model model){
        return "news/add";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String add(Model model,
                      @RequestParam("title") String title,
                      @RequestParam("content") String content,
                      @RequestParam("image") MultipartFile file,
                      RedirectAttributes redirectAttributes){
        try {
            News news = new News();
            String date = DateManager.getCurrentDate();
            String time = DateManager.getCurrentTime();
            FilenameGenerator filenameGenerator = FilenameGenerator.getInstance();
            ImageManager imageManager = ImageManager.getInstance();
            String imageName = filenameGenerator.nextString(50) + "." + file.getContentType().split("/")[1];
            news.setTitle(title);
            news.setContent(content);
            news.setDate(Date.valueOf(date));
            news.setTime(Time.valueOf(time));
            imageManager.createImage(file, FolderManager.NEWS_IMAGES_URL, imageName);
            news.setImageName(imageName);
            //TODO: ADD 'UPDATED_BY'
            newsService.add(news);
            redirectAttributes.addFlashAttribute("flash.message", "Успешно загружен: " + file.getOriginalFilename());
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("flash.message", "Ошибка загрузки: " + file.getOriginalFilename());
            e.printStackTrace();
        }
        return "news/add";
    }

}
