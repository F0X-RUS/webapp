package ru.sgmu.seem.webapp.domains;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.cache.annotation.CacheConfig;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Date;
import java.sql.Time;

@Entity
public class News extends EntityDetails{

    @NotNull
    @NotEmpty
    @Size(min=7, max=255)
    private String title;

    @Column(columnDefinition = "text")
    @Size(min=10)
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
}
