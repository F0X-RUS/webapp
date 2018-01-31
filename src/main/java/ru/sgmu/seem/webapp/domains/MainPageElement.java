package ru.sgmu.seem.webapp.domains;

import java.sql.Date;
import java.sql.Time;

public interface MainPageElement {

    Long getId();
    void setId(Long id);
    String getImageName();
    void setImageName(String imageName);
    Date getDate();
    void setDate(Date dateLastUpdate);
    Time getTime();
    void setTime(Time timeLastUpdate);
    String getUpdatedBy();
    void setUpdatedBy(String updatedBy);
}
