package com.crux.qxm.utils;

import com.github.marlonlom.utilities.timeago.TimeAgo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TimeFormatString {

    public static String getStringTime(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);

        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minutes = c.get(Calendar.MINUTE);
        String timeSet;
        if (hour > 12) {
            hour -= 12;
            timeSet = "PM";
        } else if (hour == 0) {
            hour += 12;
            timeSet = "AM";
        } else if (hour == 12) {
            timeSet = "PM";
        } else {
            timeSet = "AM";
        }

        String min;
        if (minutes < 10)
            min = "0" + minutes;
        else
            min = String.valueOf(minutes);

        String aTime = String.valueOf(hour) + ':' +
                min + " " + timeSet;

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        String dayOfTheWeek = sdf.format(c.getTime());
        String total_date;
        if (c.get(Calendar.YEAR) != Calendar.getInstance().get(Calendar.YEAR))
            total_date = new SimpleDateFormat("MMM dd, yyyy").format(c.getTime());
        else
            total_date = new SimpleDateFormat("MMM dd").format(c.getTime());

        return (aTime + "\n" + dayOfTheWeek + ", " + total_date);
    }

    public static String getDateString(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        String dayOfTheWeek = sdf.format(c.getTime());
        String total_date;
        if (c.get(Calendar.YEAR) != Calendar.getInstance().get(Calendar.YEAR))
            total_date = new SimpleDateFormat("MMM dd, yyyy").format(c.getTime());
        else
            total_date = new SimpleDateFormat("MMM dd").format(c.getTime());

        return (dayOfTheWeek + ", " + total_date);
    }

    public static String getTimeAndDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);

        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minutes = c.get(Calendar.MINUTE);
        String timeSet;
        if (hour > 12) {
            hour -= 12;
            timeSet = "PM";
        } else if (hour == 0) {
            hour += 12;
            timeSet = "AM";
        } else if (hour == 12) {
            timeSet = "PM";
        } else {
            timeSet = "AM";
        }

        String min;
        if (minutes < 10)
            min = "0" + minutes;
        else
            min = String.valueOf(minutes);

        String aTime = String.valueOf(hour) + ':' +
                min + " " + timeSet;

        String total_date;

//        today, tomorrow.. show for alarm
//        if(c.get(Calendar.DAY_OF_MONTH) == Calendar.getInstance().get(Calendar.DAY_OF_MONTH)){
//            total_date = "Today";
//        }else if(c.get(Calendar.DAY_OF_MONTH) == Calendar.getInstance().get(Calendar.DAY_OF_MONTH)+1){
//
//        }

        if (c.get(Calendar.YEAR) != Calendar.getInstance().get(Calendar.YEAR))
            total_date = new SimpleDateFormat("MMM dd, yyyy").format(c.getTime());
        else
            total_date = new SimpleDateFormat("MMM dd").format(c.getTime());

        return (aTime + ", " + total_date);
    }

    public static String getTimeAndDate(long date) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(date);

        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minutes = c.get(Calendar.MINUTE);
        String timeSet;
        if (hour > 12) {
            hour -= 12;
            timeSet = "pm";
        } else if (hour == 0) {
            hour += 12;
            timeSet = "am";
        } else if (hour == 12) {
            timeSet = "pm";
        } else {
            timeSet = "am";
        }

        String min;
        if (minutes < 10)
            min = "0" + minutes;
        else
            min = String.valueOf(minutes);

        String aTime = String.valueOf(hour) + ':' +
                min + " " + timeSet;

        String total_date;

//        today, tomorrow.. show for alarm
//        if(c.get(Calendar.DAY_OF_MONTH) == Calendar.getInstance().get(Calendar.DAY_OF_MONTH)){
//            total_date = "Today";
//        }else if(c.get(Calendar.DAY_OF_MONTH) == Calendar.getInstance().get(Calendar.DAY_OF_MONTH)+1){
//
//        }

        if (c.get(Calendar.YEAR) != Calendar.getInstance().get(Calendar.YEAR))
            total_date = new SimpleDateFormat("MMM dd, yyyy").format(c.getTime());
        else
            total_date = new SimpleDateFormat("MMM dd").format(c.getTime());

        return (aTime + ", " + total_date);
    }

    public static String getDate(long timeInMillis) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(timeInMillis);

       return new SimpleDateFormat("MMM dd, yyyy").format(c.getTime());

    }

    public static String getTimeAgo(long timeInMillis) {
        return TimeAgo.using(timeInMillis);
    }

    // region GetDurationInHourMinSec

    public static String getDurationInHourMinSec(long durationInMillis) {
        long sec = (durationInMillis / 1000) % 60;
        long min = (durationInMillis / (1000 * 60)) % 60;
        long hour = (durationInMillis / (1000 * 60 * 60)) % 24;

        String strSec = sec < 10 ? "0" + String.valueOf(sec) : String.valueOf(sec);
        String strMin = min < 10 ? "0" + String.valueOf(min) : String.valueOf(min);
        String strHour = hour < 10 ? "0" + String.valueOf(hour) : String.valueOf(hour);

        if (strHour.equals("00") && strMin.equals("00")) return String.format("%ss", strSec);
        else if (strHour.equals("00")) return String.format("%sm %ss", strMin, strSec);
        else return String.format("%sh %sm %ss", strHour, strMin, strSec);
    }

    //endregion

    // region GetDurationInHourMinSec

    public static String getDurationInHourMinSecFullForm(long durationInMillis) {
        long sec = (durationInMillis / 1000) % 60;
        long min = (durationInMillis / (1000 * 60)) % 60;
        long hour = (durationInMillis / (1000 * 60 * 60)) % 24;

        String strSec = sec < 10 ? "0" + String.valueOf(sec) : String.valueOf(sec);
        String strMin = min < 10 ? "0" + String.valueOf(min) : String.valueOf(min);
        String strHour = hour < 10 ? "0" + String.valueOf(hour) : String.valueOf(hour);

        if (sec == 0) strSec = strSec + " second";
        else strSec = strSec + " seconds";

        if (min == 0) strMin = strMin + " minute";
        else strMin = strMin + " minutes";

        if (hour == 0) strHour = strHour + " hour";
        else strHour = strHour + " hours";

        return String.format("%s %s %s", strHour, strMin, strSec);
    }

    //endregion
}
