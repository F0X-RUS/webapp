package ru.sgmu.seem.webapp.domains;


import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Date;
import java.sql.Time;

@Entity
public class Infoblock implements MainPageElement{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String imageName;

    @NotNull
    @Size(min=5, max=50)
    private String title;

    private String slogan;

    @NotNull
    @Size(min=5, max=100)
    private String description;

    private Date dateLastUpdate;

    private Time timeLastUpdate;

    private String updatedBy;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
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

    public Date getDate() {
        return dateLastUpdate;
    }

    public void setDate(Date dateLastUpdate) {
        this.dateLastUpdate = dateLastUpdate;
    }

    public Time getTime() {
        return timeLastUpdate;
    }

    public void setTime(Time timeLastUpdate) {
        this.timeLastUpdate = timeLastUpdate;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }
}
