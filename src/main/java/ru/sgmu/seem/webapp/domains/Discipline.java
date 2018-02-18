package ru.sgmu.seem.webapp.domains;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Discipline {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(min=5,max=100)
    private String title;

    @Size(min=10, max=250)
    private String description;

    private Date date;

    private Time time;

    private String updatedBy;

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "Discipline_Line",
            joinColumns = { @JoinColumn(name = "discipline_id") },
            inverseJoinColumns = { @JoinColumn(name = "line_id") }
    )
    private List<Line> lines = new ArrayList<>();


    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public List<Line> getLines() {
        return lines;
    }

    public void setLines(List<Line> lineList) {
        this.lines = lineList;
    }

}
