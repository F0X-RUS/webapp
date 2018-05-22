package ru.sgmu.seem.webapp.domains;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
public class StudyGroup extends EntityDetails{

    @NotEmpty
    @NotNull
    @Size(max = 255)
    private String title;

    @ManyToOne
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    private Set<Day> days = new HashSet<>(0);
}
