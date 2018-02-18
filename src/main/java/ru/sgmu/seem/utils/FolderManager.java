package ru.sgmu.seem.utils;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class FolderManager {

    private static final String home = System.getProperty("user.home");
    private static final Path applicationPath = Paths.get(home, "webapp");
    private static final Path imagesPath = Paths.get(home, "webapp", "images");
    private static final Path mainpageImagesPath = Paths.get(home, "webapp", "images", "main");
    public static final Path manImagesPath = Paths.get(home, "webapp", "images", "main", "man");
    public static final Path infoblockImagesPath = Paths.get(home, "webapp", "images", "main", "infoblock");
    public static final Path newsImagesPath = Paths.get(home, "webapp", "images", "news");

    private final Path default_man_image_path = Paths.get(home, "webapp", "images", "main", "man", "default.png");
    private final Path default_infoblock_image_path = Paths.get(home, "webapp", "images", "main", "infoblock", "default.png");

    public static final String INFOBLOCK_IMAGES_URL = "images/infoblock";
    public static final String MAN_IMAGES_URL = "images/man";
    public static final String NEWS_IMAGES_URL = "images/news";

    public static void createImageFolder() {
        try {
            if (!Files.exists(imagesPath)) {
                Files.createDirectories(imagesPath);
            }
            if (!Files.exists(mainpageImagesPath)) {
                Files.createDirectories(mainpageImagesPath);
            }
            if (!Files.exists(manImagesPath)) {
                Files.createDirectories(manImagesPath);
            }
            if (!Files.exists(infoblockImagesPath)) {
                Files.createDirectories(infoblockImagesPath);
            }
            if (!Files.exists(newsImagesPath)) {
                Files.createDirectories(newsImagesPath);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
