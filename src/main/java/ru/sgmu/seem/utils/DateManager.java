package ru.sgmu.seem.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateManager {

    public static String getCurrentDate(){
        DateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate = new Date();
        return date.format(currentDate);
    }

    public static String getCurrentTime(){
        DateFormat time = new SimpleDateFormat("HH:mm:ss");
        Date currentDate = new Date();
        return time.format(currentDate);
    }
}
