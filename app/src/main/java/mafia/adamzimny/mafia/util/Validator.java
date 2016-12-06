package mafia.adamzimny.mafia.util;

import android.net.Uri;
import net.danlew.android.joda.*;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by adamz on 19.08.2016.
 */
public class Validator {

    public static boolean isCorrectEmail(String email) {
        Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
        Matcher m = p.matcher(email);
        return m.matches();
    }

    public static boolean isCorrectPassword(String password) {
        //TODO validate password
        return true;
    }

    public static boolean isEmpty(String text) {
        return text.isEmpty();
    }

    public static boolean isCorrectDate(Date date) {
        return date.before(new Date());
    }

    public static boolean isCorrectUsername(String username) {
        Pattern p = Pattern.compile("^[a-zA-Z0-9_.-]*$");
        Matcher m = p.matcher(username);
        return m.matches();
    }

    public static boolean isCorrectName(String name) {
        return name.matches("[A-Z][a-zA-Z]*");
    }


}
