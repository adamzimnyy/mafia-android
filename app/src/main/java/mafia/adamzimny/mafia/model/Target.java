package mafia.adamzimny.mafia.model;


import java.util.Date;

/**
 * Created by Adam on 2016-07-15.
 */

public class Target {

    Integer id;
    User hunter;
    User hunted;
    String status;
    Date created;
    Date completed;
    boolean publicTarget = false;
    Location location;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getHunter() {
        return hunter;
    }

    public void setHunter(User hunter) {
        this.hunter = hunter;
    }

    public User getHunted() {
        return hunted;
    }

    public void setHunted(User hunted) {
        this.hunted = hunted;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getCompleted() {
        return completed;
    }

    public void setCompleted(Date completed) {
        this.completed = completed;
    }

    public boolean isPublicTarget() {
        return publicTarget;
    }

    public void setPublicTarget(boolean publicTarget) {
        this.publicTarget = publicTarget;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
