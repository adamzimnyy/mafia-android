package mafia.adamzimny.mafia.fragment;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import butterknife.OnTouch;
import mafia.adamzimny.mafia.model.UserTemplate;

import java.lang.annotation.Annotation;

/**
 * Created by adamz on 19.08.2016.
 */
public  class RegisterFragment  extends Fragment {

    private static UserTemplate userTemplate;

    public RegisterFragment withUserTemplate(UserTemplate u){
        userTemplate = u;
        return this;
    }

    public static UserTemplate getUserTemplate() {
        return userTemplate;
    }

    public void getTemplateInfo(){

    }

}
