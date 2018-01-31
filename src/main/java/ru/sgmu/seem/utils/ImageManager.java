package ru.sgmu.seem.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class ImageManager {

    public static final String DEFAULT_IMAGE = "default.png";

    private enum Extentions {
        png,
        jpg,
        jpeg,
        gif
    }

    private static ImageManager imageManager;

    private ImageManager() {

    }

    public static ImageManager getInstance() {
        if (imageManager == null) {
            imageManager = new ImageManager();
        }
        return imageManager;
    }

    public void createImage(MultipartFile file, String path, String filename) throws IOException {
        if (!file.isEmpty()) {
            Files.copy(file.getInputStream(), Paths.get(path, filename));
        }
    }

    public void deleteImage(Path filepath) throws IOException {
        if (Files.exists(filepath)) {
            Files.deleteIfExists(filepath);
        }
    }

    public boolean checkExtention(String inputExtention) {
        boolean flag = false;
        for (Extentions extention : Extentions.values()) {
            if (inputExtention.equals(extention.name())) {
                flag = true;
            }
        }
        return flag;
    }
}
