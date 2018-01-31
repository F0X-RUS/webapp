package ru.sgmu.seem.utils;

import java.util.Random;

public class FilenameGenerator {

    private Random random;

    private static FilenameGenerator filenameGenerator;

    private FilenameGenerator(){

    }

    public static FilenameGenerator getInstance(){
        if (filenameGenerator == null) {
            filenameGenerator = new FilenameGenerator();
        }
        return filenameGenerator;
    }

    public String nextString(int stringLength) {
        String charsSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder stringBuilder = new StringBuilder();
        random = new Random();
        while (stringBuilder.length() < stringLength) {
            int index = (int) (random.nextFloat() * charsSet.length());
            stringBuilder.append(charsSet.charAt(index));
        }
        return stringBuilder.toString();
    }

}
