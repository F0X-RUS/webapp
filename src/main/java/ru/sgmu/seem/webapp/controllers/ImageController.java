package ru.sgmu.seem.webapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.support.ServletContextResource;
import ru.sgmu.seem.utils.FolderManager;
import org.springframework.core.env.Environment;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static ru.sgmu.seem.utils.FolderManager.*;
import static ru.sgmu.seem.utils.ImageManager.DEFAULT_PASSAGE_IMAGE;
import static ru.sgmu.seem.utils.ImageManager.DEFAULT_USER_IMAGE;

@Controller
public class ImageController {

    private static final String FILENAME = "{filename:.+}";

    private final ResourceLoader resourceLoader;
    private Environment environment;

    @Autowired
    public ImageController(ResourceLoader resourceLoader,
                           Environment environment) {
        this.resourceLoader = resourceLoader;
        this.environment = environment;
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

    @RequestMapping(value = "images/default/" + FILENAME, method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> defaultImage(@PathVariable String filename){
        return findOne(DEFAULT_USER_IMAGE, environment.getProperty("image.path"));
    }

    @RequestMapping(value = "images/news/" + FILENAME, method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> newsOneRawImage(@PathVariable String filename) {
        return findOne(filename, newsImagesPath.toString());
    }

    @RequestMapping(value = "images/man/" + FILENAME, method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> peopleOneRawImage(@PathVariable String filename) {
        ResponseEntity<?> responseEntity = findOne(filename, manImagesPath.toString());
        if (responseEntity.getStatusCode().is4xxClientError()){
            HttpHeaders headers = new HttpHeaders();
            Resource resource = new ClassPathResource("static/images/" + DEFAULT_USER_IMAGE);
            responseEntity = new ResponseEntity<>(resource, headers, HttpStatus.OK);
        }
        return responseEntity;
    }

    @RequestMapping(value = "images/infoblock/" + FILENAME, method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> infoblockOneRawImage(@PathVariable String filename) {
        return findOne(filename, infoblockImagesPath.toString());
    }

    @RequestMapping(value = "images/staff/" + FILENAME, method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> staffOneRawImage(@PathVariable String filename) {
        ResponseEntity<?> responseEntity = findOne(filename,staffImagesPath.toString());
        if (responseEntity.getStatusCode().is4xxClientError()){
            HttpHeaders headers = new HttpHeaders();
            Resource resource = new ClassPathResource("static/images/" + DEFAULT_USER_IMAGE);
            responseEntity = new ResponseEntity<>(resource, headers, HttpStatus.OK);
        }
        return responseEntity;
    }

    @RequestMapping(value = "images/passage/" + FILENAME, method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> passageOneRawImage(@PathVariable String filename) {
        ResponseEntity<?> responseEntity = findOne(filename,passageImagesPath.toString());
        if (responseEntity.getStatusCode().is4xxClientError()){
            HttpHeaders headers = new HttpHeaders();
            Resource resource = new ClassPathResource("static/images/" + DEFAULT_PASSAGE_IMAGE);
            responseEntity = new ResponseEntity<>(resource, headers, HttpStatus.OK);
        }
        return responseEntity;
    }
}
