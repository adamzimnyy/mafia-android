package mafia.adamzimny.mafia.util;

import org.joda.time.DateTime;
import org.joda.time.Seconds;
import org.joda.time.Years;

import java.util.Date;

/**
 * Created by adamz on 18.08.2016.
 */
public class DateUtils {
    public static int getAgeFromBirthDate(Date date) {
        DateTime now = DateTime.now();
        DateTime birthday = new DateTime(date);
        Years age = Years.yearsBetween(birthday,now);
       return age.getYears();
    }

    public static int getAgeFromBirthDateLong(long date) {
        DateTime now = DateTime.now();
        DateTime birthday = new DateTime(date);
        Years age = Years.yearsBetween(birthday,now);
        return age.getYears();
    }

    public static String formatAsYMDHMS(long date){
        //milliseconds
        long different =  date - new Date().getTime();

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        return elapsedDays+"d "+elapsedHours+"h "+elapsedMinutes+"m "+elapsedSeconds+"s";
    }

    public static String timeLeftOnTargetFromCreatedDate(Date date){
        //TODO Fetch game parameters from API.
        DateTime endTime = new DateTime(date).plusDays(7);
        return formatAsYMDHMS(endTime.toDate().getTime());
    }

    public static int timeLeftOnTargetFromCreatedDateAsSeconds(Date date){
        //TODO Fetch game parameters from API.
        DateTime endTime = new DateTime(date).plusDays(7);
        DateTime now = DateTime.now();
        return Math.abs(Seconds.secondsBetween(endTime,now).getSeconds());
    }
}
