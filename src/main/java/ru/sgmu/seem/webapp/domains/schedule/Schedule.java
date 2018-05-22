package ru.sgmu.seem.webapp.domains;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Schedule extends EntityDetails {

    @NotNull
    @NotEmpty
    @Size(max = 255)
    private String title;

    @NotNull
    @NotEmpty
    private boolean weeksNumber;

    private boolean enable;

    private Date lifetime;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL)
    private Set<StudyGroup> studyGroups = new HashSet<>(0);

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isWeeksNumber() {
        return weeksNumber;
    }

    public void setWeeksNumber(boolean weeksNumber) {
        this.weeksNumber = weeksNumber;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public Date getLifetime() {
        return lifetime;
    }

    public void setLifetime(Date lifetime) {
        this.lifetime = lifetime;
    }

    public Set<StudyGroup> getStudyGroups() {
        return studyGroups;
    }

    public void setStudyGroups(Set<StudyGroup> studyGroups) {
        this.studyGroups = studyGroups;
    }
}
