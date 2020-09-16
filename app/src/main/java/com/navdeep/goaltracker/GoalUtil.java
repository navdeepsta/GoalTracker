package com.navdeep.goaltracker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class GoalUtil {
    /* To get formatted date & time of goal object */
    public static String getStartTime(Calendar calendar){
        return dayOfWeek[calendar.get(Calendar.DAY_OF_WEEK)-1]+"\n"+calendar.get(Calendar.DATE)+" "
                +months[calendar.get(Calendar.MONTH)]+" "+calendar.get(Calendar.YEAR)
                +"\n"+getTime(calendar);
    }

    private static String[] dayOfWeek = {"Invalid Day", "Monday", "Tuesday", "Wednesday", "Thursday",
            "Friday", "Saturday", "Sunday"
    };

    private static String[] months = {"Jan", "Feb", "Mar", "Apr", "May",
            "Jun", "Jul", "Aug", "Sept", "Oct",
            "Nov", "Dec"
    };
    private static String getTime(Calendar calendar) {
        int hour = calendar.get(Calendar.HOUR);
        int minutes = calendar.get(Calendar.MINUTE);
        String minute = (minutes<10) ? "0"+minutes : minutes+""; // Prefix single digit with 0
        String beforeOrAfterNoon = calendar.get(Calendar.AM_PM) == Calendar.AM? "am" : "pm";
        if(calendar.get(Calendar.AM_PM) == Calendar.PM){
            hour+=12;
        }
        return hour+":"+minute+" "+beforeOrAfterNoon;
    }


    public static Calendar getCalendarObject(String time){
        Calendar calendar = Calendar.getInstance();
        try {
            createCalendarWithFormattedTime(calendar, time);
        }catch (ParseException pe){
            pe.printStackTrace();
        }
        return  calendar;
    }

    private static void createCalendarWithFormattedTime(Calendar calendar, String time)
            throws ParseException{
                String format = "EEE MMM dd HH:mm:ss z yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(format);
                Date date = sdf.parse(time);
                calendar.setTime(date);
    }

}
