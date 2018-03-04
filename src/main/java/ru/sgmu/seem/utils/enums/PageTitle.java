package ru.sgmu.seem.utils.enums;

public enum PageTitle {

    //TODO: MOVE TO RESOURCE

    MAIN("Главная страница"),
    EDIT_MAN("Изменить превью сотрудника на главной"),
    EDIT_INFOBLOCK("Изменить информационный блок на главной"),
    VIEW("Обзор"),
    CONTACTS("Контакты"),
    STAFF("Сотрудники"),
    EDIT_STAFF("Изменить данные сотрудника"),
    ADD_STAFF("Добавить нового сотрудника"),
    ADD_CONTACTS("Добавить контакт"),
    ADD_PASSAGE("Добавить абзац"),
    NEWS("Новости"),
    ADD_NEWS("Добавить новость"),
    EDIT_NEWS("Изменить новость"),
    ABOUT("О кафедре"),
    EDIT_PASSAGE("Изменить содержание абзаца"),
    FORUM("Форум");

    private String text;

    PageTitle(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
