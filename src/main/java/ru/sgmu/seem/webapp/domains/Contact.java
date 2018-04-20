package ru.sgmu.seem.webapp.domains;

import org.springframework.cache.annotation.CacheConfig;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Date;
import java.sql.Time;

@Entity
@AttributeOverride(name = "imageName", column = @Column(name = "image_name", insertable = false, updatable = false))
public class Contact extends EntityDetails{

    @NotNull
    @ManyToOne
    @JoinColumn(name = "contact_type_id", nullable = false)
    private ContactType type;

    @NotNull
    @Size(min=3)
    @Column(length = 1000)
    private String content;

    public Contact(){}

    public Contact(ContactType type, String content) {
        this.type = type;
        this.content = content;
    }

    public ContactType getType() {
        return type;
    }

    public void setType(ContactType type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
