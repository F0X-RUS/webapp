package ru.sgmu.seem.utils;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static ru.sgmu.seem.utils.FolderManager.NEWS_IMAGES_URL;
import static ru.sgmu.seem.utils.FormValidator.IMAGE_CHOOSE_FILE;
import static ru.sgmu.seem.utils.FormValidator.IMAGE_WRONG_FORMAT;

@Component
public class ImageManager {

    private final FilenameGenerator filenameGenerator;

    public static final String DEFAULT_USER_IMAGE = "defaultuser.png";
    public static final String DEFAULT_INFOBLOCK_IMAGE = "defaultinfoblock.svg";
    public static final String DEFAULT_PASSAGE_IMAGE = "defaultpassage.png";

    private enum Extentions {
        PNG,
        JPG,
        JPEG,
        GIF
    }

    @Autowired
    public ImageManager(FilenameGenerator filenameGenerator) {
        this.filenameGenerator = filenameGenerator;
    }

    public void createImage(MultipartFile file, String path, String filename) throws IOException {
        try {
            if (!file.isEmpty()) {
                Files.copy(file.getInputStream(), Paths.get(path, filename));
            }
        } catch (FileAlreadyExistsException faee) {
            faee.printStackTrace();
        }
    }

    public void deleteImage(Path filepath) throws IOException {
        if (Files.exists(filepath)) {
            Files.deleteIfExists(filepath);
        }
    }

    public boolean checkExtention(MultipartFile file) {
        String fileExtention = file.getContentType().split("/")[1].toUpperCase();
        boolean flag = false;
        for (Extentions extention : Extentions.values()) {
            if (fileExtention.equals(extention.name())) {
                flag = true;
            }
        }
        return flag;
    }

    public String parseFile(MultipartFile file) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        if (!checkExtention(file)) {
            throw new IOException("Wrong extention!");
        }
        String name = filenameGenerator.nextString();
        stringBuilder
                .append(name)
                .append(".")
                .append(file.getContentType().split("/")[1].toUpperCase());
        return stringBuilder.toString();
    }

    public boolean checkSelectedImage(MultipartFile file) {
        boolean check = true;
        String extension = file.getContentType().split("/")[1].toUpperCase();
        if (file.isEmpty() || !checkExtention(file)) {
            check = false;
        }
        return check;
    }

    public String getImageName(MultipartFile file, Path imagesFolder){
        String imageName = null;
        try {
            imageName = parseFile(file);
            createImage(file, imagesFolder.toString(), imageName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageName;
    }
}
