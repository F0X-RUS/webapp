package ru.sgmu.seem.webapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.sgmu.seem.utils.FolderManager;

import java.io.IOException;

import static ru.sgmu.seem.utils.FolderManager.*;

@Controller
public class ImageController {

    private static final String FILENAME = "{filename:.+}";

    private ResourceLoader resourceLoader;

    @Autowired
    public ImageController(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    private Resource findOneImage(String filename, String path) {
        return resourceLoader.getResource("file:" + path + "/" + filename);
    }

    //TODO: THINK ABOUT NEXT CODE

    @RequestMapping(value = "images/news/" + FILENAME, method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> newsOneRawImage(@PathVariable String filename) {
        try {
            Resource file = this.findOneImage(filename, FolderManager.getNewsImagesPath().toString());
            return ResponseEntity.ok().contentLength(file.contentLength())
                    .contentType(MediaType.IMAGE_JPEG).body(new InputStreamResource(file.getInputStream()));
        } catch (IOException ioe) {
            return ResponseEntity.badRequest().body("Изображение не найдено: " + ioe.getMessage());
        }
    }

    @RequestMapping(value = "images/man" + "/" + FILENAME, method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> peopleOneRawImage(@PathVariable String filename) {
        try {
            Resource file = this.findOneImage(filename, FolderManager.getManImagesPath().toString());
            return ResponseEntity.ok().contentLength(file.contentLength())
                    .contentType(MediaType.IMAGE_JPEG).body(new InputStreamResource(file.getInputStream()));
        } catch (IOException ioe) {
            return ResponseEntity.badRequest().body("Изображение не найдено: " + ioe.getMessage());
        }
    }

    @RequestMapping(value = "images/infoblock/" + FILENAME, method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> infoblockOneRawImage(@PathVariable String filename) {
        try {
            Resource file = this.findOneImage(filename, FolderManager.getInfoblockImagesPath().toString());
            return ResponseEntity.ok().contentLength(file.contentLength())
                    .contentType(MediaType.IMAGE_JPEG).body(new InputStreamResource(file.getInputStream()));
        } catch (IOException ioe) {
            return ResponseEntity.badRequest().body("Изображение не найдено: " + ioe.getMessage());
        }
    }
}
