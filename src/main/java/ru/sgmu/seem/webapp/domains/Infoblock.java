package ru.sgmu.seem.webapp.domains;


import org.springframework.cache.annotation.CacheConfig;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Date;
import java.sql.Time;

@Entity
public class Infoblock extends EntityDetails {

    @NotNull
    @Size(min=5, max=50)
    private String title;

    private String slogan;

    @Column(length = 65535)
    @Size(min=5)
    private String description;

    public Infoblock(){}

    public Infoblock(String title, String slogan, String description) {
        this.title = title;
        this.slogan = slogan;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
