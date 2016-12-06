package mafia.adamzimny.mafia;

import android.content.res.Configuration;
import com.squareup.leakcanary.LeakCanary;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by adamz on 18.08.2016.
 */
public class Application extends android.app.Application {

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/ProductSans.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        LeakCanary.install(this);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

}
