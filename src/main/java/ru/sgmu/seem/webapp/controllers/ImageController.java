package ru.sgmu.seem.webapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.sgmu.seem.utils.FolderManager;

import java.io.IOException;

import static ru.sgmu.seem.utils.FolderManager.*;

@Controller
public class ImageController {

    private static final String FILENAME = "{filename:.+}";

    private final ResourceLoader resourceLoader;

    @Autowired
    public ImageController(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    private ResponseEntity<?> findOne(String filename, String path) {
        try {
            Resource file = resourceLoader.getResource("file:" + path + "/" + filename);
            return ResponseEntity.ok().contentLength(file.contentLength())
                    .contentType(MediaType.IMAGE_JPEG).body(new InputStreamResource(file.getInputStream()));
        } catch (IOException ioe) {
            return ResponseEntity.badRequest().body("Изображение не найдено: " + ioe.getMessage());
        }
    }

    @RequestMapping(value = "images/news/" + FILENAME, method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> newsOneRawImage(@PathVariable String filename) {
        return findOne(filename, newsImagesPath.toString());
    }

    @RequestMapping(value = "images/man/" + FILENAME, method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> peopleOneRawImage(@PathVariable String filename) {
        return findOne(filename, manImagesPath.toString());
    }

    @RequestMapping(value = "images/infoblock/" + FILENAME, method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> infoblockOneRawImage(@PathVariable String filename) {
        return findOne(filename, infoblockImagesPath.toString());
    }

    @RequestMapping(value = "images/staff/" + FILENAME, method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> staffOneRawImage(@PathVariable String filename) {
        return findOne(filename, staffImagesPath.toString());
    }
}
