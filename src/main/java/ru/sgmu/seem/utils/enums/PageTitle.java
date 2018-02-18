package ru.sgmu.seem.utils.enums;

public enum PageTitle {
    MAIN("Главная страница"),
    VIEW("Обзор"),
    CONTACTS("Контакты"),
    ADD_CONTACTS("Добавить контакт"),
    ADD_PASSAGE("Добавить абзац"),
    NEWS("Новости"),
    ADD_NEWS("Добавить новость"),
    EDIT_NEWS("Изменить новость"),
    DELETE_NEWS("Удалить новость"),
    ABOUT("О кафедре");

    private String text;

    PageTitle(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
