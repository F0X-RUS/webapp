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
    public static final Path staffImagesPath = Paths.get(home, "webapp", "images", "staff");
    public static final Path passageImagesPath = Paths.get(home, "webapp", "images", "passage");
    public static final Path threadFilesPath = Paths.get(home, "webapp", "files", "thread");

    public static final String INFOBLOCK_IMAGES_URL = "images/infoblock";
    public static final String MAN_IMAGES_URL = "images/man";
    public static final String NEWS_IMAGES_URL = "images/news";
    public static final String STAFF_IMAGES_URL = "images/staff";
    public static final String PASSAGE_IMAGES_URL = "images/passage";
    public static final String THREAD_FILES_URL = "files/thread";

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
            if (!Files.exists(staffImagesPath)) {
                Files.createDirectories(staffImagesPath);
            }
            if (!Files.exists(passageImagesPath)) {
                Files.createDirectories(passageImagesPath);
            }
            if (!Files.exists(threadFilesPath)) {
                Files.createDirectories(threadFilesPath);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
