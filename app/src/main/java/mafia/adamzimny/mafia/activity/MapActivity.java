package mafia.adamzimny.mafia.activity;

import android.content.Context;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import mafia.adamzimny.mafia.R;
import mafia.adamzimny.mafia.api.RetrofitBuilder;
import mafia.adamzimny.mafia.api.service.LocationService;
import mafia.adamzimny.mafia.model.Location;
import mafia.adamzimny.mafia.model.MapMarker;
import mafia.adamzimny.mafia.model.Target;
import mafia.adamzimny.mafia.util.AppVariable;
import mafia.adamzimny.mafia.util.DateUtils;
import mafia.adamzimny.mafia.util.RetroDate;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    Context context;
    Target target;
    private GoogleMap map;
    ClusterManager<MapMarker> mClusterManager;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        context = this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        target = (Target) getIntent().getSerializableExtra("target");
        getHuntedLocations();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        setUpClusterer();
    }

    public void getHuntedLocations() {
        LocationService service = (LocationService) RetrofitBuilder.getService(LocationService.class, RetrofitBuilder.BASE_URL);
        //TODO pobranie parametru z bazy
        //TODO pobieranie najnowszych lokalizacji, parsowanie daty
        Call<List<Location>> call = service.findByUserAndDateAfter(target.getHunted().getId(), new RetroDate(DateUtils.daysAgo(500)), AppVariable.token);
       // Call<List<Location>> call = service.findByUser(target.getHunted().getId(), AppVariable.token);

        call.enqueue(new Callback<List<Location>>() {
            @Override
            public void onResponse(Call<List<Location>> call, Response<List<Location>> response) {
                if (response.code() == 200) {
                    List<Location> list = response.body();
                    for (Location l : list) {
                        Log.d("location", "Adding marker " + l.getLatitude() + " " + l.getLongitude());

                        if (DateUtils.daysSince(l.getDate().getTime()) > 6) {
                            if (AppVariable.useMarkerClusters) {
                                mClusterManager.addItem(new MapMarker(l.getLatitude(),
                                        l.getLongitude(), DateUtils.date(l.getDate().getTime()),
                                        DateUtils.hour(l.getDate().getTime())));
                            } else {
                                map.addMarker(new MarkerOptions()
                                        .position(new LatLng(l.getLatitude(), l.getLongitude()))
                                        .title(DateUtils.date(l.getDate().getTime()))
                                        .snippet(DateUtils.hour(l.getDate().getTime())));
                            }
                        } else {
                            if (AppVariable.useMarkerClusters) {
                                mClusterManager.addItem(new MapMarker(l.getLatitude(),
                                        l.getLongitude(), android.text.format.DateUtils.getRelativeTimeSpanString(l.getDate().getTime()) + "",
                                        DateUtils.format(l.getDate().getTime())));
                            } else {
                                map.addMarker(new MarkerOptions()
                                        .position(new LatLng(l.getLatitude(), l.getLongitude()))
                                        .title(android.text.format.DateUtils.getRelativeTimeSpanString(l.getDate().getTime()) + "")
                                        .snippet( DateUtils.format(l.getDate().getTime())));
                            }
                        }
                    }
                    map.animateCamera(CameraUpdateFactory.zoomBy(0));
                } else {
                    Log.d("location", "Status " + response.code() + " " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Location>> call, Throwable t) {
                Log.d("location", "failure");
            }
        });
    }

    private void setUpClusterer() {
        // Declare a variable for the cluster manager.

        // Position the map.
        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        mClusterManager = new ClusterManager<>(this, map);

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        map.setOnCameraIdleListener(mClusterManager);
        map.setOnMarkerClickListener(mClusterManager);

        mClusterManager.setRenderer(new OwnRendring(getApplicationContext(), map, mClusterManager));
        // Add cluster items (markers) to the cluster manager.
    }


    public class OwnRendring extends DefaultClusterRenderer<MapMarker> {

        public OwnRendring(Context context, GoogleMap map,
                           ClusterManager<MapMarker> clusterManager) {
            super(context, map, clusterManager);
        }


        protected void onBeforeClusterItemRendered(MapMarker item, MarkerOptions markerOptions) {

            markerOptions.snippet(item.getSnippet());
            markerOptions.title(item.getTitle());
            super.onBeforeClusterItemRendered(item, markerOptions);
        }
    }
}

