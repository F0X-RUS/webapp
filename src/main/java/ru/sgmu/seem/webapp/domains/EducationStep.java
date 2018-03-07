package ru.sgmu.seem.webapp.domains;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@AttributeOverride(name = "imageName", column = @Column(name = "image_name", insertable = false, updatable = false))
public class EducationStep extends EntityDetails{

    @Size(max = 255)
    private String name;

    @Column(length = 65535)
    private String description;

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "EducationStep_Course",
            joinColumns = { @JoinColumn(name = "educationstep_id") },
            inverseJoinColumns = { @JoinColumn(name = "course_id") }
    )
    private Set<Course> courses = new HashSet<>(0);

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "EducationStep_Specialization",
            joinColumns = { @JoinColumn(name = "educationstep_id") },
            inverseJoinColumns = { @JoinColumn(name = "specialization_id") }
    )
    private Set<Specialization> specializations = new HashSet<>(0);

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "EducationStep_Discipline",
            joinColumns = { @JoinColumn(name = "educationstep_id") },
            inverseJoinColumns = { @JoinColumn(name = "discipline_id") }
    )
    private Set<Discipline> disciplines = new HashSet<>(0);

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "EducationStep_Thread",
            joinColumns = { @JoinColumn(name = "educationstep_id") },
            inverseJoinColumns = { @JoinColumn(name = "thread_id") }
    )
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
}
