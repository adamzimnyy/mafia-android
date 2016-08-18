package mafia.adamzimny.mafia.model;

import java.util.Date;

/**
 * Created by adamz on 02.08.2016.
 */
public class Code {

    Integer id;

    User source;

    User destination;
    String code;
    Date created;
    Location location;
}
