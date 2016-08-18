package mafia.adamzimny.mafia.model;


import java.util.Date;

/**
 * Created by adamz on 02.08.2016.
 */
public class Reward {
    Integer id;

    User player;
    int value;
    Date added;
    String awardedFor;

    Target target;
}
