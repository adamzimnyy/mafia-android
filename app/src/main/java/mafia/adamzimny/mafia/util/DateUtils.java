package mafia.adamzimny.mafia.util;

import android.util.Log;
import android.util.StringBuilderPrinter;
import org.joda.time.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by adamz on 18.08.2016.
 */
public class DateUtils {
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    static final long WEEK = 24 * 60 * 60 * 7;
    static final long DAY = 24 * 60 * 60;
    static final long HOUR = 60 * 60;
    static final long MINUTE = 60;
    static final long MONTH = 30 * DAY;

    public static int getAgeFromBirthDate(Date date) {
        DateTime now = DateTime.now();
        DateTime birthday = new DateTime(date);
        Years age = Years.yearsBetween(birthday, now);
        Log.d("years", "years between " + now + " and " + birthday + " is " + age.getYears());
        return age.getYears();
    }

    public static String format(long date) {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date(date);
        return sdfDate.format(now);
    }

    public static String date(long date) {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
        Date now = new Date(date);
        return sdfDate.format(now);
    }

    public static String hour(long date) {
        SimpleDateFormat sdfDate = new SimpleDateFormat("HH:mm:ss");
        Date now = new Date(date);
        return sdfDate.format(now);
    }

    public static String formatRelative(long date) {

        if (minutesSince(date) < MINUTE) {
            return android.text.format.DateUtils.getRelativeTimeSpanString(date, new Date().getTime(), android.text.format.DateUtils.SECOND_IN_MILLIS) + "";
        } else if (hoursSince(date) < HOUR) {
            return android.text.format.DateUtils.getRelativeTimeSpanString(date, new Date().getTime(), android.text.format.DateUtils.MINUTE_IN_MILLIS) + "";
        } else if (daysSince(date) < DAY) {
            return android.text.format.DateUtils.getRelativeTimeSpanString(date, new Date().getTime(), android.text.format.DateUtils.HOUR_IN_MILLIS) + "";
        } else if (weeksSince(date) < WEEK) {
            return android.text.format.DateUtils.getRelativeTimeSpanString(date, new Date().getTime(), android.text.format.DateUtils.DAY_IN_MILLIS) + "";
        } else if (monthsSince(date) < MONTH) {
            return android.text.format.DateUtils.getRelativeTimeSpanString(date, new Date().getTime(), android.text.format.DateUtils.WEEK_IN_MILLIS) + "";
        } else {
            return android.text.format.DateUtils.getRelativeTimeSpanString(date, new Date().getTime(), android.text.format.DateUtils.YEAR_IN_MILLIS) + "";
        }
    }

    public static int minutesSince(long date) {
        DateTime now = DateTime.now();
        DateTime datee = new DateTime(date);
        Minutes age = Minutes.minutesBetween(datee, now);
        return age.getMinutes();
    }

    public static int hoursSince(long date) {
        DateTime now = DateTime.now();
        DateTime datee = new DateTime(date);
        Hours age = Hours.hoursBetween(datee, now);
        return age.getHours();
    }

    public static int monthsSince(long date) {
        DateTime now = DateTime.now();
        DateTime datee = new DateTime(date);
        Months age = Months.monthsBetween(datee, now);
        return age.getMonths();
    }


    public static int weeksSince(long date) {
        DateTime now = DateTime.now();
        DateTime datee = new DateTime(date);
        Weeks age = Weeks.weeksBetween(datee, now);
        return age.getWeeks();
    }

    public static int daysSince(long date) {
        DateTime now = DateTime.now();
        DateTime datee = new DateTime(date);
        Days age = Days.daysBetween(datee, now);
        return age.getDays();
    }

    public static int yearsSince(long date) {
        DateTime now = DateTime.now();
        DateTime birthday = new DateTime(date);
        Years age = Years.yearsBetween(birthday, now);
        Log.d("years", "years between " + now + " and " + birthday + " is " + age.getYears());

        return age.getYears();
    }

    public static String formatMillisAsYMDHMS(long millis) {
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = millis / daysInMilli;
        millis = millis % daysInMilli;

        long elapsedHours = millis / hoursInMilli;
        millis = millis % hoursInMilli;

        long elapsedMinutes = millis / minutesInMilli;
        millis = millis % minutesInMilli;

        long elapsedSeconds = millis / secondsInMilli;
        StringBuilder sb = new StringBuilder();
        if (elapsedDays != 0) sb.append(elapsedDays).append("d ");
        if (elapsedHours != 0) sb.append(elapsedHours).append("h ");
        if (elapsedMinutes != 0) sb.append(elapsedMinutes).append("m ");
        if (elapsedSeconds != 0) sb.append(elapsedSeconds).append("s ");
        return sb.toString().trim();
    }

    public static String formatAsYMDHMS(long date) {
        //milliseconds
        long different = date - new Date().getTime();
        return formatMillisAsYMDHMS(different);
    }

    public static String timeLeftOnTargetFromCreatedDate(Date date) {
        //TODO Fetch game parameters from API.
        LocalDateTime endTime = new LocalDateTime(date).plusDays(7);
        return formatAsYMDHMS(endTime.toDate().getTime());
    }

    public static int timeLeftOnTargetFromCreatedDateAsSeconds(Date date) {
        //TODO Fetch game parameters from API.
        LocalDateTime endTime = new LocalDateTime(date).plusDays(7);
        LocalDateTime now = LocalDateTime.now();
        return 1000*Math.abs(Seconds.secondsBetween(endTime, now).getSeconds());
    }

    public static Date daysAgoAsLong(int days) {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -1 * days);
        return cal.getTime();
    }

    public static Date daysAgo(int days) {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -1 * days);
        return cal.getTime();
    }
}
