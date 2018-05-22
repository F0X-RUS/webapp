package ru.sgmu.seem.webapp.domains.main;

import org.springframework.cache.annotation.CacheConfig;
import ru.sgmu.seem.webapp.domains.EntityDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Date;
import java.sql.Time;

@Entity
public class Man extends EntityDetails {

    @NotNull
    @Size(min=1, max=25)
    private String name;

    @NotNull
    @Size(min=1, max=25)
    private String surname;

    private String patronymic;

    @Column(length = 65535)
    @Size(min=5)
    private String description;

    public Man(){}

    public Man(String name, String surname, String patronymic, String description) {
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
