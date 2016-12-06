package mafia.adamzimny.mafia.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Adam on 2016-07-15.
 */

public class Location implements Serializable{

    Integer id;
    double latitude;
    double longitude;
    String type;
    Date date;
    User user;

    public Location() {
    }

    public Location(double latitude, double longitute) {
        this.latitude = latitude;
        this.longitude = longitute;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
