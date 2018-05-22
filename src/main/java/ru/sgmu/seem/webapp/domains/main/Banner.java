package ru.sgmu.seem.webapp.domains;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Banner extends EntityDetails {

    @NotNull
    @Size(min=5, max=50)
    private String title;

    @NotNull
    @Size(min=5, max=200)
    private String link;

    @Size(max=100)
    private String description;

    public Banner() {
    }

    public Banner(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
