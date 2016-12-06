package mafia.adamzimny.mafia.util;

import android.location.LocationListener;
import android.location.LocationManager;
import mafia.adamzimny.mafia.model.Location;
import mafia.adamzimny.mafia.model.User;

import java.util.Date;

/**
 * Created by adamz on 18.08.2016.
 */
public class AppVariable {
    public static User loggedUser;
    public static String token;
    public static String code;
    public static long codeTimeLeft;
    public static Location lastKnownLocation;
    public static Date lastKnownLocationDate;
    public static boolean loginLocationSaved;
    public static boolean useMarkerClusters = true;
    public static LocationManager locationManager;
    public static LocationListener locationListener;
}
