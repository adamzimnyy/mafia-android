package mafia.adamzimny.mafia.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RetroDate {
    private static final ThreadLocal<DateFormat> DF = new ThreadLocal<DateFormat>() {
        @Override public DateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss zzz");
        }
    };

    private final Date date;

    public RetroDate(Date date) {
        this.date = date;
    }

    @Override public String toString() {
        return DF.get().format(date);
    }
}
