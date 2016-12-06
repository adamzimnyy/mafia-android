package mafia.adamzimny.mafia.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mafia.adamzimny.mafia.R;
import mafia.adamzimny.mafia.api.RetrofitBuilder;
import mafia.adamzimny.mafia.api.service.LocationService;
import mafia.adamzimny.mafia.constant.Locations;
import mafia.adamzimny.mafia.constant.Params;
import mafia.adamzimny.mafia.model.Location;
import mafia.adamzimny.mafia.util.AppVariable;
import mafia.adamzimny.mafia.util.DateUtils;
import mafia.adamzimny.mafia.util.helper.IntentHelper;
import mafia.adamzimny.mafia.util.helper.LocationHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.solodovnikov.rxlocationmanager.LocationRequestBuilder;
import ru.solodovnikov.rxlocationmanager.LocationTime;
import ru.solodovnikov.rxlocationmanager.RxLocationManager;
import rx.Subscriber;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class GpsActivity extends AppCompatActivity implements LocationListener {

    android.location.Location lastKnown;
    boolean exitLoop = false;

    @BindView(R.id.settings)
    Button settingsButton;

    @BindView(R.id.status_text)
    TextView statusText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);
        ButterKnife.bind(this);
        AppVariable.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        AppVariable.locationListener = this;
        getLocation();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AppVariable.loginLocationSaved = false;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
           return;
        }
        AppVariable.locationManager.removeUpdates(this);

    }

    private void getLocation() {
        //TODO zmienić UI odpowiednio do wykonywanej części


        boolean gpsPermission = checkPermissions();
        if (!gpsPermission) {
            Log.d(getClass().getSimpleName(), "No GPS permissions!");

            //TODO Handle permissions result
            requestPemissions();
            return;
        }
        boolean gpsEnabled = checkSettings();
        if (!gpsEnabled) {
            Log.d(getClass().getSimpleName(), "GPS not enabled!");

            //TODO Handle settings result
            buildAlertMessageNoGps();
            return;
        }
        //  subscribeForLocation(25f, this);
        getLocationUpdates();
    }

    private void requestPemissions() {
        Log.d(getClass().getSimpleName(), "Requesting permissions...");
        statusText.setText("Requesting permissions...");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, IntentHelper.PERMISSION_GPS);
        }

        Log.d(getClass().getSimpleName(), "Done.");

    }

    private boolean checkSettings() {
        statusText.setText("Checking settings...");
        Log.d(getClass().getSimpleName(), "Checking settings...");
        return AppVariable.locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private boolean checkPermissions() {
        statusText.setText("Checking permissions...");
        Log.d(getClass().getSimpleName(), "Checking permissions...");
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void buildAlertMessageNoGps() {
        Log.d(getClass().getSimpleName(), "Showing GPS settings alert...");
        statusText.setText("Enable GPS to continue.");

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), IntentHelper.GPS_SETTINGS);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public void getLocationUpdates() {
        statusText.setText("Waiting on GPS fix...");

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        AppVariable.locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, 0, 0, this);
        AppVariable.locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    private void saveLoginLocation() {
        Log.d("location", "Saving location");
        LocationService service = (LocationService) RetrofitBuilder.getService(LocationService.class, RetrofitBuilder.BASE_URL);
        Location loc = LocationHelper.map(lastKnown, Locations.LOGIN);
        Call<Void> saveCall = service.save(loc, AppVariable.token);
        saveCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d("locations", "Response!" + response.code());

                if (response.code() == 200) {
                    Log.d("locations", "Saved!");
                    goToTargets();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d(getClass().getSimpleName(), "Location saving error! " + t.getLocalizedMessage());
            }
        });
    }

    int i = 0;

    @Override
    public void onLocationChanged(android.location.Location location) {
        Log.d(getClass().getSimpleName(), "update location " + i++ + " " + location.getAccuracy() + " " + location.getLatitude() + " " + location.getLongitude());
        if (location.getAccuracy() < Params.LOCATION_ACCURACY) {
            Log.d(getClass().getSimpleName(), "Accurate!");
            AppVariable.lastKnownLocation = new Location(location.getLatitude(), location.getLongitude());
            AppVariable.lastKnownLocationDate = new Date();
            lastKnown = location;
            if (!AppVariable.loginLocationSaved) {
                saveLoginLocation();
                AppVariable.loginLocationSaved = true;
            }
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    public void goToTargets() {
        IntentHelper.startActivityIntent(this, TargetActivity.class);
        finish();
    }
}
