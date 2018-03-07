package ru.sgmu.seem.webapp.domains;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@AttributeOverride(name = "imageName", column = @Column(name = "image_name", insertable = false, updatable = false))
public class Discipline extends EntityDetails {

    @Size(max = 255)
    private String name;

    @Column(length = 65535)
    private String description;

    @ManyToMany(mappedBy = "disciplines")
    private Set<EducationStep> educationSteps = new HashSet<>(0);

    @ManyToMany(mappedBy = "disciplines")
    private Set<Course> courses = new HashSet<>(0);

    @ManyToMany(mappedBy = "disciplines")
    private Set<Specialization> specializations = new HashSet<>(0);

    @OneToMany(mappedBy = "discipline")
    private Set<Thread> threads = new HashSet<>(0);

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

    public Set<Thread> getThreads() {
        return threads;
    }

    public void setThreads(Set<Thread> threads) {
        this.threads = threads;
    }
}
