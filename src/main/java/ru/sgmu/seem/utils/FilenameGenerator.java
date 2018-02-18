package ru.sgmu.seem.utils;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class FilenameGenerator {

    public String nextString(int stringLength) {
        String charsSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        while (stringBuilder.length() < stringLength) {
            int index = (int) (random.nextFloat() * charsSet.length());
            stringBuilder.append(charsSet.charAt(index));
        }
        return stringBuilder.toString();
    }

}
