package mafia.adamzimny.mafia.model;


import java.util.Date;

/**
 * Created by adamz on 02.08.2016.
 */
public class Report {

    Integer id;

    User player;
    User reportedBy;
    String reason;
    String description;
    Date date;

}
