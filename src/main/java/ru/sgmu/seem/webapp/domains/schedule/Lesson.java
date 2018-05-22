package ru.sgmu.seem.webapp.domains;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Lesson extends EntityDetails{

    @NotNull
    @NotEmpty
    @Size(max = 255)
    private String title;

    @NotNull
    @NotEmpty
    @Size(max = 255)
    private String place;

    @NotNull
    @NotEmpty
    @Size(max = 255)
    private String time;

    @NotNull
    @NotEmpty
    @Size(max = 255)
    private String teacher;

    @ManyToOne
    @JoinColumn(name = "day_id", nullable = false)
    private Day day;
}
