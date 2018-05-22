package ru.sgmu.seem.webapp.domains.forum;

import ru.sgmu.seem.webapp.domains.EntityDetails;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@AttributeOverride(name = "imageName", column = @Column(name = "image_name", insertable = false, updatable = false))
public class EducationStep extends EntityDetails {

    @Size(min = 1, max = 255)
    private String name;

    @Column(length = 65535)
    private String description;

    @OneToMany(mappedBy = "educationStep", cascade = CascadeType.ALL)
    private Set<Course> courses = new HashSet<>(0);

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Course> getCourses() {
        return courses;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }
}
