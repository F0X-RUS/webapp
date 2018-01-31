package ru.sgmu.seem.webapp.controllers;

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
import ru.sgmu.seem.webapp.domains.Infoblock;
import ru.sgmu.seem.webapp.domains.News;
import ru.sgmu.seem.webapp.domains.Man;
import ru.sgmu.seem.webapp.services.CrudService;
import ru.sgmu.seem.webapp.services.NewsService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.sql.Time;
import java.util.*;

import static ru.sgmu.seem.utils.FolderManager.INFOBLOCK_IMAGES_URL;
import static ru.sgmu.seem.utils.FolderManager.MAN_IMAGES_URL;


@Controller
@RequestMapping(value = "/editor")
public class EditorController {

    private static final String MAIN_PAGE_TITLE = "Главная страница";
    private static final String VIEW_PAGE_TITLE = "Обзор";
    private static final String NEWS_PAGE_TITLE = "Новости";
    private static final String ADD_NEWS_PAGE_TITLE = "Добавить новость";
    private static final String EDIT_NEWS_PAGE_TITLE = "Изменить новость";
    private static final String DELETE_NEWS_PAGE_TITLE = "Удалить новость";
    private static final String VIEW_FRAGMENT_PATH = "editor/fragments/default";
    private static final String NEWS_FRAGMENT_PATH = "editor/fragments/news";
    private static final String ADD_NEWS_FRAGMENT_PATH = "editor/fragments/news-add";
    private static final String EDIT_NEWS_FRAGMENT_PATH = "editor/fragments/news-edit";
    private static final String DELETE_NEWS_FRAGMENT_PATH = "editor/fragments/news-delete";
    private static final String MAIN_FRAGMENT_PATH = "editor/fragments/main";
    private static final String VIEW_CONTENT_ELEMENT = "default";
    private static final String NEWS_CONTENT_ELEMENT = "news";
    private static final String MAIN_CONTENT_ELEMENT = "main";
    private static final String CONTENT_ATTRIBUTE = "content";
    private static final String CONTENT_ELEMENT_ATTRIBUTE = "content_element";
    private static final String TITLE_ATTRIBUTE = "title";
    private static final String NEWS_IMAGES_DIR = "news_images_dir";
    private static final String MAN_IMAGES_DIR = "man_upload_directory";
    private static final String INFOBLOCK_IMAGES_DIR = "infoblock_upload_directory";
    private static final String IMAGE_ERROR = "image_error";
    private static final String IMAGE_ERROR_MESSAGE = "Wrong image format!";

    @Qualifier("manService")
    @Autowired
    private CrudService<Man> manService;

    @Qualifier("newsServiceImpl")
    @Autowired
    private NewsService newsService;

    @Qualifier("infoblockService")
    @Autowired
    private CrudService<Infoblock> infoblockService;


    @RequestMapping(method = RequestMethod.GET)
    public String index(HttpServletRequest request, Model model) {
        model.addAttribute(CONTENT_ATTRIBUTE, VIEW_FRAGMENT_PATH);
        model.addAttribute(CONTENT_ELEMENT_ATTRIBUTE, VIEW_CONTENT_ELEMENT);
        model.addAttribute(TITLE_ATTRIBUTE, VIEW_PAGE_TITLE);
        return "editor/index";
    }

    @RequestMapping(value = "/news", method = RequestMethod.GET)
    public String news(Model model, Pageable pageable) {
        Map<String, String> pageParams = new HashMap<>();
        Map<String, String> pageFilePaths = new HashMap<>();
        pageParams.put(CONTENT_ATTRIBUTE, NEWS_FRAGMENT_PATH);
        pageParams.put(CONTENT_ELEMENT_ATTRIBUTE, NEWS_CONTENT_ELEMENT);
        pageParams.put(TITLE_ATTRIBUTE, NEWS_PAGE_TITLE);
        pageFilePaths.put(NEWS_IMAGES_DIR, FolderManager.NEWS_IMAGES_URL);
        Page<News> newsPage = newsService.getPage(pageable.getPageNumber(),
                10,
                Sort.Direction.DESC,
                "date", "time");
        PageWrapper<News> page = new PageWrapper<>(newsPage, "news", 5);
        model.addAttribute("news", page.getContent());
        model.addAttribute("page", page);

        model.addAllAttributes(pageParams);
        model.addAllAttributes(pageFilePaths);
        return "editor/index";

    }

    @RequestMapping(value = "/news/add", method = RequestMethod.GET)
    public String newsAdd(Model model) {
        Map<String, String> pageParams = new HashMap<>();
        Map<String, String> pageFilePaths = new HashMap<>();
        pageParams.put(CONTENT_ATTRIBUTE, ADD_NEWS_FRAGMENT_PATH);
        pageParams.put(CONTENT_ELEMENT_ATTRIBUTE, NEWS_CONTENT_ELEMENT);
        pageParams.put(TITLE_ATTRIBUTE, ADD_NEWS_PAGE_TITLE);
        pageFilePaths.put(NEWS_IMAGES_DIR, FolderManager.NEWS_IMAGES_URL);
        model.addAllAttributes(pageParams);
        model.addAllAttributes(pageFilePaths);
        return "editor/index";
    }

    @RequestMapping(value = "/news/add", params = "submit", method = RequestMethod.POST)
    public RedirectView newsAddSubmit(Model model,
                                      @Valid @ModelAttribute("news") News news,
                                      BindingResult bindingResult,
                                      @RequestParam("image") MultipartFile file,
                                      RedirectAttributes redirectAttributes) throws IOException {
        try {
            Map<String, String> errors = FormValidator.getInstance().getMap(bindingResult);
            if (!file.isEmpty()) {
                String extention = file.getContentType().split("/")[1];
                String name = FilenameGenerator.getInstance().nextString(50);
                String imageName = name + "." + extention;
                if (!ImageManager.getInstance().checkExtention(extention)) {
                    throw new IOException();
                }
                ImageManager.getInstance().createImage(file, FolderManager.NEWS_IMAGES_URL, imageName);
                news.setImageName(imageName);
                news.setDate(Date.valueOf(DateManager.getCurrentDate()));
                news.setTime(Time.valueOf(DateManager.getCurrentTime()));
            } else {
                errors.put(IMAGE_ERROR, "Need to select some image");
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
            redirectAttributes.addFlashAttribute("message", "Ошибка загрузки: " + file.getOriginalFilename());
            return new RedirectView("/editor/news/add", true);
        }
    }

    @RequestMapping(value = "/news/add", params = "cancel", method = RequestMethod.POST)
    public RedirectView newsAddCancel(Model model) {
        return new RedirectView("/editor/news", true);
    }

    @RequestMapping(value = "/news/{id}/edit", method = RequestMethod.GET)
    public String newsEdit(Model model,
                           @PathVariable("id") Long id) {
        Map<String, String> pageParams = new HashMap<>();
        Map<String, String> pageFilePaths = new HashMap<>();
        pageParams.put(CONTENT_ATTRIBUTE, EDIT_NEWS_FRAGMENT_PATH);
        pageParams.put(CONTENT_ELEMENT_ATTRIBUTE, NEWS_CONTENT_ELEMENT);
        pageParams.put(TITLE_ATTRIBUTE, EDIT_NEWS_PAGE_TITLE);
        pageFilePaths.put(NEWS_IMAGES_DIR, FolderManager.NEWS_IMAGES_URL);
        News news = newsService.getById(id);
        model.addAttribute("news", news);
        model.addAllAttributes(pageParams);
        model.addAllAttributes(pageFilePaths);
        return "editor/index";
    }

    @RequestMapping(value = "/news/edit", params = "submit", method = RequestMethod.POST)
    public RedirectView newsEdit(@Valid @ModelAttribute("news") News news,
                                 BindingResult bindingResult,
                                 @RequestParam("id") Long id,
                                 @RequestParam("title") String title,
                                 @RequestParam("content") String content,
                                 @RequestParam("image") MultipartFile file,
                                 RedirectAttributes redirectAttributes) throws IOException {
        try {
            if (bindingResult.hasErrors()) {
                Map<String, String> errors = FormValidator.getInstance().getMap(bindingResult);
                for (Map.Entry<String, String> entry : errors.entrySet()) {
                    redirectAttributes.addFlashAttribute(entry.getKey(), entry.getValue());
                }
                return new RedirectView("/editor/news/" + id + "/edit", true);
            }
            news.setDate(Date.valueOf(DateManager.getCurrentDate()));
            news.setTime(Time.valueOf(DateManager.getCurrentTime()));
            if (!file.isEmpty()) {
                String extention = file.getContentType().split("/")[1];
                String name = FilenameGenerator.getInstance().nextString(50);
                String imageName = name + "." + extention;
                if (!ImageManager.getInstance().checkExtention(extention)) {
                    throw new IOException();
                }
                ImageManager.getInstance().createImage(file, FolderManager.NEWS_IMAGES_URL, imageName);
                Path oldImage = Paths.get(FolderManager.NEWS_IMAGES_URL, newsService.getById(news.getId()).getImageName());
                if (Files.exists(oldImage)) {
                    ImageManager.getInstance().deleteImage(oldImage);
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

    @RequestMapping(value = "/news/edit", params = "cancel", method = RequestMethod.POST)
    public RedirectView newsEdit() {
        return new RedirectView("/editor/news", true);
    }

    @RequestMapping(value = "/news/{id}/delete", method = RequestMethod.GET)
    public String newsDelete(Model model,
                             @PathVariable("id") Long id) {
        Map<String, String> pageParams = new HashMap<>();
        Map<String, String> pageFilePaths = new HashMap<>();
        pageParams.put(CONTENT_ATTRIBUTE, DELETE_NEWS_FRAGMENT_PATH);
        pageParams.put(CONTENT_ELEMENT_ATTRIBUTE, NEWS_CONTENT_ELEMENT);
        pageParams.put(TITLE_ATTRIBUTE, DELETE_NEWS_PAGE_TITLE);
        pageFilePaths.put(NEWS_IMAGES_DIR, FolderManager.NEWS_IMAGES_URL);
        News news = newsService.getById(id);
        model.addAttribute("news", news);
        model.addAllAttributes(pageParams);
        model.addAllAttributes(pageFilePaths);
        return "editor/index";
    }

    @RequestMapping(value = "/news/delete", params = "submit", method = RequestMethod.POST)
    public RedirectView newsDelete(@RequestParam("id") Long id) {
        newsService.remove(id);
        return new RedirectView("/editor/news", true);
    }

    @RequestMapping(value = "/news/delete", params = "cancel", method = RequestMethod.POST)
    public RedirectView newsDelete() {
        return new RedirectView("/editor/news", true);
    }

    @RequestMapping(value = "/main", method = RequestMethod.GET)
    public String main(HttpServletRequest request,
                       Model model) {
        Map<String, String> pageParams = new HashMap<>();
        Map<String, String> pageFilePaths = new HashMap<>();
        pageParams.put(CONTENT_ATTRIBUTE, MAIN_FRAGMENT_PATH);
        pageParams.put(CONTENT_ELEMENT_ATTRIBUTE, MAIN_CONTENT_ELEMENT);
        pageParams.put(TITLE_ATTRIBUTE, MAIN_PAGE_TITLE);
        pageFilePaths.put(MAN_IMAGES_DIR, MAN_IMAGES_URL);
        pageFilePaths.put(INFOBLOCK_IMAGES_DIR, FolderManager.getInstance().INFOBLOCK_IMAGES_URL);
        List<Man> manList = manService.getAll();
        List<Infoblock> infoblockList = infoblockService.getAll();
        manList.sort((e1, e2) -> (Long.compare(e1.getId(), e2.getId())));
        infoblockList.sort((e1, e2) -> (Long.compare(e1.getId(), e2.getId())));
        model.addAttribute("man_list", manList);
        model.addAttribute("infoblock_list", infoblockList);
        model.addAllAttributes(pageParams);
        model.addAllAttributes(pageFilePaths);
        return "editor/index";
    }

    @RequestMapping(value = "/main/man/{id}", method = RequestMethod.POST)
    public String main(@Valid @ModelAttribute("man") Man man,
                       BindingResult bindingResult,
                       Model model,
                       @PathVariable("id") Long formId,
                       @RequestParam("image") MultipartFile file,
                       RedirectAttributes redirectAttributes) throws IOException {
        Map<String, String> errors = FormValidator.getInstance().getMap(bindingResult);
        errors.remove("imageName_error");
        String oldImageName = manService.getById(man.getId()).getImageName();
        String newImageExtention = file.getContentType().split("/")[1];
        String imageName = FormValidator.getInstance().checkImage(oldImageName, file);
        if (!file.isEmpty() && !ImageManager.getInstance().checkExtention(newImageExtention)) {
            errors.put(IMAGE_ERROR, IMAGE_ERROR_MESSAGE);
        }
        man.setImageName(imageName);
        if (errors.size() != 0) {
            for (Map.Entry<String, String> entry : errors.entrySet()) {
                redirectAttributes.addFlashAttribute(entry.getKey().split("_")[0] + formId + "_error", entry.getValue());
                System.out.println(entry.getKey().split("_")[0] + formId + "_error");
            }
        } else {
            man.setDate(Date.valueOf(DateManager.getCurrentDate()));
            man.setTime(Time.valueOf(DateManager.getCurrentTime()));
            manService.update(man);
            ImageManager.getInstance().createImage(file, MAN_IMAGES_URL, imageName);
        }
        return "redirect:/editor/main";
    }

    @RequestMapping(value = "/main/infoblock/{id}", method = RequestMethod.POST)
    public String main(@Valid @ModelAttribute("infoblock") Infoblock infoblock,
                       BindingResult bindingResult,
                       Model model,
                       @PathVariable("id") Long formId,
                       @RequestParam("image") MultipartFile file,
                       RedirectAttributes redirectAttributes) throws IOException {
        Map<String, String> errors = FormValidator.getInstance().getMap(bindingResult);
        errors.remove("imageName_error");
        String oldImageName = infoblockService.getById(infoblock.getId()).getImageName();
        String newImageExtention = file.getContentType().split("/")[1];
        String imageName = FormValidator.getInstance().checkImage(oldImageName, file);
        if (!file.isEmpty() && !ImageManager.getInstance().checkExtention(newImageExtention)) {
            errors.put(IMAGE_ERROR, IMAGE_ERROR_MESSAGE);
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
            ImageManager.getInstance().createImage(file, INFOBLOCK_IMAGES_URL, imageName);
        }
        return "redirect:/editor/main#infoform" + formId;
    }
}
