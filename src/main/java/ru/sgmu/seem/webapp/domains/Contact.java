package ru.sgmu.seem.webapp.domains;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Date;
import java.sql.Time;

@Entity
@AttributeOverride(name = "imageName", column = @Column(name = "image_name", insertable = false, updatable = false))
public class Contact extends EntityDetails{

    @NotNull
    private String type;

    @NotNull
    @Size(min=3)
    @Column(length = 1000)
    private String content;

    public String getType() {
        return type;
    }

    public void setType(String name) {
        this.type = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
