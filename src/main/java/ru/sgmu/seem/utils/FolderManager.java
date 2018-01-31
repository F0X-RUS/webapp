package ru.sgmu.seem.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FolderManager {

    /*
    * Create folder "webapp" in home user directory
    */

    private static final String home = System.getProperty("user.home");
    private static final Path applicationPath = Paths.get(home, "webapp");
    private static final Path imagesPath = Paths.get(home, "webapp", "images");
    private static final Path mainpageImagesPath = Paths.get(home, "webapp", "images", "main");
    private static final Path manImagesPath = Paths.get(home, "webapp", "images", "main", "man");
    private static final Path infoblockImagesPath = Paths.get(home, "webapp", "images", "main", "infoblock");
    private static final Path newsImagesPath = Paths.get(home, "webapp", "images", "news");

    private final Path default_man_image_path = Paths.get(home, "webapp", "images", "main", "man", "default.png");
    private final Path default_infoblock_image_path = Paths.get(home, "webapp", "images", "main", "infoblock", "default.png");

    public static final String INFOBLOCK_IMAGES_URL = "images/infoblock";
    public static final String MAN_IMAGES_URL = "images/man";
    public static final String NEWS_IMAGES_URL = "images/news";


    private static FolderManager folderManager;


    private FolderManager() {
    }

    public static FolderManager getInstance() {
        if (folderManager == null) {
            folderManager = new FolderManager();
        }
        return folderManager;
    }

    public void createImageFolder() {
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

    public static Path getManImagesPath() {
        return manImagesPath;
    }

    public static Path getInfoblockImagesPath() {
        return infoblockImagesPath;
    }

    public static Path getNewsImagesPath() {
        return newsImagesPath;
    }
}
