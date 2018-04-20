package ru.sgmu.seem.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.sql.Date;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;

public class DateManager {

    private static Calendar calendar = Calendar.getInstance();

    public static Date getCurrentDate(){
        return new Date(calendar.getTimeInMillis());
    }

    public static Time getCurrentTime(){
        return new Time(calendar.getTimeInMillis());
    }


}
