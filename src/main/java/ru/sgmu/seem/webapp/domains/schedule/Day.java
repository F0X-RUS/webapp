package ru.sgmu.seem.webapp.domains;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Day extends EntityDetails{

    @NotNull
    @NotEmpty
    @Size(max = 255)
    private String weekday;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private StudyGroup studyGroup;

    @OneToMany(mappedBy = "day", cascade = CascadeType.ALL)
    private Set<Lesson> lessons = new HashSet<>(0);
}
