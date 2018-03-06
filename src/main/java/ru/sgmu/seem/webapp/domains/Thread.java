package ru.sgmu.seem.webapp.domains;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@AttributeOverride(name = "imageName", column = @Column(name = "image_name", insertable = false, updatable = false))
public class Thread extends EntityDetails {

    @Size(max = 255)
    private String name;

    @Column(length = 65535)
    private String description;

//    private List<String> fileNames;

    @ManyToMany(mappedBy = "threads")
    private Set<EducationStep> educationSteps = new HashSet<>(0);

    @ManyToMany(mappedBy = "threads")
    private Set<Course> courses = new HashSet<>(0);

    @ManyToMany(mappedBy = "threads")
    private Set<Specialization> specializations = new HashSet<>(0);

    @ManyToOne
    @JoinColumn(name = "discipline_id", nullable = false)
    private Discipline discipline;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return description;
    }

    public void setContent(String content) {
        this.description = content;
    }

    public Set<EducationStep> getEducationSteps() {
        return educationSteps;
    }

    public void setEducationSteps(Set<EducationStep> educationSteps) {
        this.educationSteps = educationSteps;
    }

    public Set<Course> getCourses() {
        return courses;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }

    public Set<Specialization> getSpecializations() {
        return specializations;
    }

    public void setSpecializations(Set<Specialization> specializations) {
        this.specializations = specializations;
    }

    public Discipline getDiscipline() {
        return discipline;
    }

    public void setDiscipline(Discipline discipline) {
        this.discipline = discipline;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
