package ru.sgmu.seem.webapp.domains;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@AttributeOverride(name = "imageName", column = @Column(name = "image_name", insertable = false, updatable = false))
public class Course extends EntityDetails{

    @Size(max = 255)
    private String name;

    @Column(length = 65535)
    private String description;

    @ManyToMany(mappedBy = "courses")
    private Set<EducationStep> educationSteps = new HashSet<>(0);

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "EducationStep_Course_Specialization",
            joinColumns = { @JoinColumn(name = "course_id") },
            inverseJoinColumns = { @JoinColumn(name = "specialization_id") }
    )
    private Set<Specialization> specializations = new HashSet<>(0);

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "EducationStep_Course_Specialization_discipline",
            joinColumns = { @JoinColumn(name = "course_id") },
            inverseJoinColumns = { @JoinColumn(name = "discipline_id") }
    )
    private Set<Discipline> disciplines = new HashSet<>(0);

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "EducationStep_Course_Specialization_discipline_thread",
            joinColumns = { @JoinColumn(name = "course_id") },
            inverseJoinColumns = { @JoinColumn(name = "thread_id") }
    )
    private Set<Thread> threads = new HashSet<>(0);

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<EducationStep> getEducationSteps() {
        return educationSteps;
    }

    public void setEducationSteps(Set<EducationStep> educationSteps) {
        this.educationSteps = educationSteps;
    }

    public Set<Specialization> getSpecializations() {
        return specializations;
    }

    public void setSpecializations(Set<Specialization> specializations) {
        this.specializations = specializations;
    }

    public Set<Discipline> getDisciplines() {
        return disciplines;
    }

    public void setDisciplines(Set<Discipline> disciplines) {
        this.disciplines = disciplines;
    }

    public Set<Thread> getThreads() {
        return threads;
    }

    public void setThreads(Set<Thread> threads) {
        this.threads = threads;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}