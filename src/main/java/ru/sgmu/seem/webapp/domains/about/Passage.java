package ru.sgmu.seem.webapp.domains;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.cache.annotation.CacheConfig;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Date;
import java.sql.Time;

@Entity
public class Passage extends EntityDetails{

    @Column(length = 400)
    @Size(min = 4, max = 250)
    private String title;

    @Size(min = 1, max = 20)
    @NotEmpty
    @NotNull
    private String shortcut;

    @NotEmpty
    @Column(columnDefinition = "text")
    @Size(min = 15)
    private String content;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getShortcut() {
        return shortcut;
    }

    public void setShortcut(String shortcut) {
        this.shortcut = shortcut;
    }
}
