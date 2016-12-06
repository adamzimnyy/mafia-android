package mafia.adamzimny.mafia.model;

import java.util.Date;

/**
 * Created by adamz on 02.08.2016.
 */
public class Code {

    Integer id;
    Date created;
    Location location;
    User user;
    String code;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }


    public static final class Builder {
        Integer id;
        User user;
        String codeString;
        Date created;
        Location location;

        private Builder() {
        }

        public static Builder create() {
            return new Builder();
        }

        public Builder withId(Integer id) {
            this.id = id;
            return this;
        }

        public Builder withUser(User user){
            this.user = user;
            return this;
        }

        public Builder withCode(String code) {
            this.codeString = code;
            return this;
        }

        public Builder withCreated(Date created) {
            this.created = created;
            return this;
        }

        public Builder withLocation(Location location) {
            this.location = location;
            return this;
        }

        public Code build() {
            Code code = new Code();
            code.setId(id);
            code.setUser(user);
            code.setCode(codeString);
            code.setCreated(created);
            code.setLocation(location);
            return code;
        }
    }
}
