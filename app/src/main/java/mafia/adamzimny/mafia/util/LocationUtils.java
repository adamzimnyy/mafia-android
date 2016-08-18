package mafia.adamzimny.mafia.util;

import mafia.adamzimny.mafia.api.RetrofitBuilder;
import mafia.adamzimny.mafia.api.service.LocationService;
import mafia.adamzimny.mafia.model.Location;

/**
 * Created by adamz on 18.08.2016.
 */
public class LocationUtils {

    public static float distance(Location a, Location b) {
        if (a == null || b == null) return 10;
        android.location.Location locationA = new android.location.Location("");

        locationA.setLatitude(a.getLatitude());
        locationA.setLongitude(a.getLongitute());

        android.location.Location locationB = new android.location.Location("");

        locationB.setLatitude(b.getLatitude());
        locationB.setLongitude(b.getLongitute());

        return locationA.distanceTo(locationB);
    }
}
