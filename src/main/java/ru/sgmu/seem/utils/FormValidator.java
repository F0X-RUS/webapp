package ru.sgmu.seem.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
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
    public static final String IMAGE_UPLOAD_ERROR = "Ошибка при загрузке изображения!";

    private final ImageManager imageManager;

    @Autowired
    public FormValidator(ImageManager imageManager){
        this.imageManager = imageManager;
    }

    public boolean checkImage(MultipartFile file, BindingResult bindingResult, Model model){
        if (!imageManager.checkSelectedImage(file)){
            model.addAttribute("image_err", IMAGE_UPLOAD_ERROR);
        }
        return !bindingResult.hasErrors() && !model.containsAttribute("image_err");
    }
}
