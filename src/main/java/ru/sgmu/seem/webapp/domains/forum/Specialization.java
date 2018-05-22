package ru.sgmu.seem.webapp.domains.forum;

import ru.sgmu.seem.webapp.domains.EntityDetails;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@AttributeOverride(name = "imageName", column = @Column(name = "image_name", insertable = false, updatable = false))
public class Specialization extends EntityDetails {

    @Size(min = 1, max = 255)
    private String name;

    @Column(length = 65535)
    private String description;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @OneToMany(mappedBy = "specialization", cascade = CascadeType.ALL)
    private Set<Discipline> disciplines = new HashSet<>(0);

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

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Set<Discipline> getDisciplines() {
        return disciplines;
    }

    public void setDisciplines(Set<Discipline> disciplines) {
        this.disciplines = disciplines;
    }

}
