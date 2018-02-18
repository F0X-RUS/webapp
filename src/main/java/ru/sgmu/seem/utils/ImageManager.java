package ru.sgmu.seem.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static ru.sgmu.seem.utils.FolderManager.NEWS_IMAGES_URL;

@Component
public class ImageManager {

    private final FilenameGenerator filenameGenerator;

    public static final String DEFAULT_IMAGE = "default.png";

    private enum Extentions {
        PNG,
        JPG,
        JPEG,
        GIF
    }

    @Autowired
    public ImageManager(FilenameGenerator filenameGenerator){
        this.filenameGenerator = filenameGenerator;
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

    public String parseFile(MultipartFile file) throws IOException{

        String extention = file.getContentType().split("/")[1];
        if (!checkExtention(extention.toUpperCase())) {
            throw new IOException("Wrong extention!");
        }
        String name = filenameGenerator.nextString(50);
        return name + "." + extention;
    }
}
