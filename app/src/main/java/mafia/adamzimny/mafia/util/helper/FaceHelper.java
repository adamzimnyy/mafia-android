package mafia.adamzimny.mafia.util.helper;

import android.graphics.Bitmap;
import com.google.android.gms.vision.face.Face;

/**
 * Created by adamz on 20.09.2016.
 */
public class FaceHelper {

    public static float getFaceSizePercent(Face face, Bitmap bitmap) {
        float faceSize = face.getHeight() * face.getWidth();
        float imageSize = bitmap.getHeight() * bitmap.getWidth();
        return faceSize*100.0f/imageSize;
    }


}
