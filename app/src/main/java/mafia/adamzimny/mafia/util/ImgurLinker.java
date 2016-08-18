package mafia.adamzimny.mafia.util;

import java.io.File;

/**
 * Created by adamz on 18.08.2016.
 */
public class ImgurLinker {

    public static String compileUrl(String hash) {
        return "https://i.imgur.com/" + hash + ".png";
    }
}
