package com.crux.qxm.utils;

import java.util.Calendar;
import java.util.Date;

public class QxmDateHelper {
    public static long getDateOnly(Date date){

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        return calendar.getTimeInMillis();
    }
}
