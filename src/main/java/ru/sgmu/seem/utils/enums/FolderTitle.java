package ru.sgmu.seem.utils.enums;

public enum FolderTitle {

    //TODO: THINK ABOUT MOVE TO RESOURCE

    NEWS_IMAGES("news_images_path"),
    MAN_IMAGES("man_images_path"),
    INFOBLOCK_IMAGES("infoblock_images_path"),
    STAFF_IMAGES("staff_images_path");

    private String text;

    FolderTitle(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
