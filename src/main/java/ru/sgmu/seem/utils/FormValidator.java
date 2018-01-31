package ru.sgmu.seem.utils;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

public class FormValidator {

    private static FormValidator formValidator;

    private FormValidator(){
    }

    public static FormValidator getInstance(){
        if (formValidator == null){
            formValidator = new FormValidator();
        }
        return formValidator;
    }

    public Map<String, String> getMap(BindingResult bindingResult){
        Map<String, String> errorList = new HashMap<>();
        for (int i = 0; i < bindingResult.getAllErrors().size(); i++) {
            if (bindingResult.getAllErrors().get(i) instanceof FieldError){
                errorList.put(
                        ((FieldError) bindingResult.getAllErrors().get(i)).getField() + "_error",
                        ((FieldError) bindingResult.getAllErrors().get(i)).getField());
            }
        }
        return errorList;
    }

    public String checkImage(String oldImageName, MultipartFile image){
        String newImageName;
        if (!image.isEmpty()) {
            String newFileName = FilenameGenerator.getInstance().nextString(50);
            String extention = image.getContentType().split("/")[1];
            if (!ImageManager.getInstance().checkExtention(extention)){
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
