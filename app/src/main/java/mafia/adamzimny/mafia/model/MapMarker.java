package mafia.adamzimny.mafia.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by adamz on 09.09.2016.
 */
public class MapMarker implements ClusterItem {
    private final LatLng mPosition;
    String title;
    String snippet;

    public MapMarker(double lat, double lng,String tit ,String sni) {
        mPosition = new LatLng(lat, lng);
        title = tit;
        snippet = sni;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }
}