package ru.sgmu.seem.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

import static ru.sgmu.seem.utils.ImageManager.*;

@Component
public class FormValidator {

    public static final String IMAGE_ERROR = "image_error";
    public static final String IMAGE_WRONG_FORMAT = "Неверный формат изображения!";
    public static final String IMAGE_CHOOSE_FILE = "Требуется выбрать изображение!";

    private final FilenameGenerator filenameGenerator;
    private final ImageManager imageManager;

    @Autowired
    public FormValidator(FilenameGenerator filenameGenerator, ImageManager imageManager){
        this.filenameGenerator = filenameGenerator;
        this.imageManager = imageManager;
    }

    public static Map<String, String> getMap(BindingResult bindingResult){
        Map<String, String> errorList = new HashMap<>();
        for (int i = 0; i < bindingResult.getAllErrors().size(); i++) {
            if (bindingResult.getAllErrors().get(i) instanceof FieldError){
                errorList.put(((FieldError) bindingResult.getAllErrors().get(i)).getField() + "_error",
                        bindingResult.getAllErrors().get(i).getDefaultMessage());
            }
        }
        return errorList;
    }

    public String checkImage(String oldImageName, MultipartFile image){
        String newImageName;
        if (!image.isEmpty()) {
            String newFileName = filenameGenerator.nextString(50);
            String extention = image.getContentType().split("/")[1];
            if (!imageManager.checkExtention(extention)){
                newImageName = oldImageName;
            } else {
                newImageName = newFileName + "." + extention;
            }
        } else {
            newImageName = oldImageName;
        }
        return newImageName;
    }
}
