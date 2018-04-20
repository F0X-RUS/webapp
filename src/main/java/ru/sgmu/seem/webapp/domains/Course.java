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

    @Size(min = 1, max = 255)
    private String name;

    @Column(length = 65535)
    private String description;

    @ManyToOne
    @JoinColumn(name = "educationStep_id", nullable = false)
    private EducationStep educationStep;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private Set<Specialization> specializations = new HashSet<>(0);

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EducationStep getEducationStep() {
        return educationStep;
    }

    public void setEducationStep(EducationStep educationStep) {
        this.educationStep = educationStep;
    }

    public Set<Specialization> getSpecializations() {
        return specializations;
    }

    public void setSpecializations(Set<Specialization> specializations) {
        this.specializations = specializations;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
