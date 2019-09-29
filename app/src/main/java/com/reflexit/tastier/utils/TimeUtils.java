package com.reflexit.tastier.utils;

import java.util.Calendar;

public class TimeUtils {

    public static StringBuilder dateDifference(long date) {
        Calendar current = Calendar.getInstance();
        long difference = current.getTimeInMillis() - date;
        Calendar calendarDiff = Calendar.getInstance();
        calendarDiff.setTimeInMillis(difference);

        StringBuilder dateDifference = new StringBuilder("Last visit was ");
        int initialLength = dateDifference.length();
        int dayDifference = calendarDiff.get(Calendar.DAY_OF_MONTH) - 1;
        int hoursDiff = calendarDiff.get(Calendar.HOUR) - 2;
        int minutesDiff = calendarDiff.get(Calendar.MINUTE);

        if (dayDifference == 0 && hoursDiff == 0 && minutesDiff < 1) {
            dateDifference.append("a moment ago");
            return dateDifference;
        }

        if (dayDifference != 0) {
            dateDifference.append(dayDifference).append(" days ");
        }

        if (hoursDiff != 0) {
            if (dateDifference.length() > initialLength) {
                dateDifference.append("and ");
            }
            dateDifference.append(hoursDiff).append(" hours ");
        }

        if (minutesDiff >= 1) {
            if (dateDifference.length() > initialLength) {
                dateDifference.append("and ");
            }
            dateDifference.append(minutesDiff).append(" minutes ");
        }
        dateDifference.append("ago");

        return dateDifference;
    }


    public static int daysDifference(long date) {
        Calendar current = Calendar.getInstance();
        long difference = current.getTimeInMillis() - date;
        Calendar calendarDiff = Calendar.getInstance();
        calendarDiff.setTimeInMillis(difference);
        return calendarDiff.get(Calendar.DAY_OF_MONTH) - 1;
    }

}
