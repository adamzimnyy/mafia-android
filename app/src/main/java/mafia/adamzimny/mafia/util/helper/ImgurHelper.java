package mafia.adamzimny.mafia.util.helper;

import java.io.File;

/**
 * Created by adamz on 18.08.2016.
 */
public class ImgurHelper {

    public static String compileUrl(String hash) {
        return "https://i.imgur.com/" + hash + ".png";
    }
}
