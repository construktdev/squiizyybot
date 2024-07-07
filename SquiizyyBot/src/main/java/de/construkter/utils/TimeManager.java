package de.construkter.utils;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TimeManager {
    public static String gettimestamp(){
        LocalTime currentTime = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return currentTime.format(formatter);
    }
}
