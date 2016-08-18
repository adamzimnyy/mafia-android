package mafia.adamzimny.mafia.model;

import java.util.Date;

/**
 * Created by Adam on 2016-07-15.
 */

public class Location {

    Integer id;
    long latitude;
    long longitute;
    String type;
    Date date;
    User user;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public long getLatitude() {
        return latitude;
    }

    public void setLatitude(long latitude) {
        this.latitude = latitude;
    }

    public long getLongitute() {
        return longitute;
    }

    public void setLongitute(long longitute) {
        this.longitute = longitute;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
