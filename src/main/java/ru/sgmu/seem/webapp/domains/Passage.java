package ru.sgmu.seem.webapp.domains;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.sql.Date;
import java.sql.Time;

@Entity
@AttributeOverride(name = "imageName", column = @Column(name = "image_name", insertable = false, updatable = false))
public class Passage extends EntityDetails{

    @Column(length = 400)
    @Size(min = 10, max = 400)
    private String title;

    @NotEmpty
    @Column(length = 65535)
    @Size(min = 20)
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
