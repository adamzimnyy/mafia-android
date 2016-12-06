package mafia.adamzimny.mafia.util.helper;

import mafia.adamzimny.mafia.model.Location;
import mafia.adamzimny.mafia.model.Target;
import mafia.adamzimny.mafia.util.AppVariable;

import java.util.Date;

/**
 * Created by adamz on 18.08.2016.
 */
public class LocationHelper {

    public static String distance(Location a, Location b) {
        if (a == null || b == null) return "?";
        android.location.Location locationA = new android.location.Location("");

        locationA.setLatitude(a.getLatitude());
        locationA.setLongitude(a.getLongitude());

        android.location.Location locationB = new android.location.Location("");

        locationB.setLatitude(b.getLatitude());
        locationB.setLongitude(b.getLongitude());

        return String.valueOf(locationA.distanceTo(locationB));
    }

    public static Location map(android.location.Location location, String type) {
        Location result = new Location(location.getLatitude(),location.getLongitude());
        result.setDate(new Date());
        result.setType(type);
        result.setUser(AppVariable.loggedUser);
        return result;
    }


    public static String distance(Target target) {
        android.location.Location locationA = new android.location.Location("");
        locationA.setLatitude(target.getHunted().getLatitude());
        locationA.setLongitude(target.getHunted().getLongitude());
        android.location.Location locationB = new android.location.Location("");
        locationB.setLatitude(AppVariable.lastKnownLocation.getLatitude());
        locationB.setLongitude(AppVariable.lastKnownLocation.getLongitude());

        return String.valueOf(locationA.distanceTo(locationB));
    }
}
