package mafia.adamzimny.mafia.activity;

import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.google.android.gms.maps.GoogleMap;
import com.nostra13.universalimageloader.core.ImageLoader;
import mafia.adamzimny.mafia.R;
import mafia.adamzimny.mafia.api.RetrofitBuilder;
import mafia.adamzimny.mafia.api.service.TargetService;
import mafia.adamzimny.mafia.constant.Params;
import mafia.adamzimny.mafia.constant.Targets;
import mafia.adamzimny.mafia.model.Target;
import mafia.adamzimny.mafia.model.User;
import mafia.adamzimny.mafia.model.json.TargetConfirmation;
import mafia.adamzimny.mafia.util.AppVariable;
import mafia.adamzimny.mafia.util.DateUtils;
import mafia.adamzimny.mafia.util.helper.ImageLoaderHelper;
import mafia.adamzimny.mafia.util.helper.ImgurHelper;
import mafia.adamzimny.mafia.util.helper.IntentHelper;
import mafia.adamzimny.mafia.util.helper.LocationHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsActivity extends AppCompatActivity {

    private GoogleMap map;
    CountDownTimer timer;
    Target target;
    User hunted;
    @BindView(R.id.name_text)
    TextView nameText;

    @BindView(R.id.distance_text)
    TextView distanceText;

    @BindView(R.id.age_text)
    TextView ageText;

    @BindView(R.id.time_text)
    TextView timeText;

    @BindView(R.id.status_bar)
    View statusBarView;

    @BindView(R.id.code_text)
    TextView codeLabel;

    @BindView(R.id.image_view)
    ImageView profilePicture;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.send_button)
    Button sendButton;

    @BindView(R.id.code_field)
    EditText codeField;

    //  SupportMapFragment mapFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initView(null);
/*
        mapFragment  = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mapFragment.getView().setMinimumHeight(mapFragment.getView().getWidth());*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            finish();
        }
        if (menuItem.getItemId() == R.id.action_map) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("target", target);
            IntentHelper.startActivityIntent(this, MapActivity.class, bundle);
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public boolean onPrepareOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.target_details_toolbar_icons, menu);

        return super.onCreateOptionsMenu(menu);
    }

    private void initView(Target t) {

        target = t == null ? (Target) getIntent().getSerializableExtra("target") : t;
        hunted = this.target.getHunted();
        ImageLoaderHelper.initialize(this);
        ImageLoader.getInstance().displayImage(ImgurHelper.compileUrl(hunted.getProfilePicture()), profilePicture);
        distanceText.setText(String.valueOf(LocationHelper.distance(target)));
        nameText.setText(hunted.getFirstName() + " " + hunted.getLastName());
        ageText.setText(DateUtils.getAgeFromBirthDate(hunted.getDateOfBirth()) + "");
        codeField.setVisibility(View.INVISIBLE);
        codeLabel.setVisibility(View.INVISIBLE);
        sendButton.setVisibility(View.INVISIBLE);
        Log.d("target", "initView with target: " + t);
        Log.d("target", "status: " + target.getStatus());
        switch (target.getStatus()) {
            case Targets.ACTIVE:
                timer = new CountDownTimer(DateUtils.timeLeftOnTargetFromCreatedDateAsSeconds(target.getCreated()), 1000) {

                    public void onTick(long millisUntilFinished) {
                        timeText.setText(DateUtils.timeLeftOnTargetFromCreatedDate(target.getCreated()));
                    }

                    public void onFinish() {

                    }
                };
                timer.start();
                statusBarView.setBackgroundColor(ContextCompat.getColor(this, R.color.target_yellow));
                codeField.setVisibility(View.VISIBLE);
                codeLabel.setVisibility(View.VISIBLE);
                sendButton.setVisibility(View.VISIBLE);
                break;
            case Targets.FAILED:
                Log.d("target", "details status failed");
                timeText.setText(Targets.FAILED);
                statusBarView.setBackgroundColor(ContextCompat.getColor(this, R.color.target_red));
                if (timer != null) timer.cancel();
                break;
            case Targets.COMPLETED:
                timeText.setText(Targets.COMPLETED);
                Log.d("target", "details status failed");
                statusBarView.setBackgroundColor(ContextCompat.getColor(this, R.color.target_green));
                if (timer != null) timer.cancel();
                break;
        }
    }

    @OnClick(R.id.send_button)
    public void confirmTarget() {
        TargetService service = (TargetService) RetrofitBuilder.getService(TargetService.class, RetrofitBuilder.BASE_URL);
        Call<Target> confirmCall = service.confirm(target.getId(), codeField.getText().toString().trim().toUpperCase(), AppVariable.token);
        confirmCall.enqueue(new Callback<Target>() {
            @Override
            public void onResponse(Call<Target> call, Response<Target> response) {
                if (response.code() == 200 && response.body().getCompleted() != null) {
                    target = response.body();
                    initView(target);
                } else {
                    Toast.makeText(DetailsActivity.this, "Code incorrect. Try again in " + Params.WRONG_CODE_RETRY_TIME_SECONDS + " seconds.", Toast.LENGTH_LONG).show();
                    sendButton.setEnabled(false);
                    sendButton.setTextColor(ContextCompat.getColor(DetailsActivity.this, R.color.placeholder_text));
                    CountDownTimer timer = new CountDownTimer(Params.WRONG_CODE_RETRY_TIME_SECONDS * 1000, 1000) {
                        @Override
                        public void onTick(long l) {
                            Log.d("code", "delay update! " + l + " left");
                            sendButton.setText(DateUtils.formatMillisAsYMDHMS(l));
                            sendButton.invalidate();
                        }

                        @Override
                        public void onFinish() {
                            Log.d("code", "delay done!");
                            sendButton.setText(R.string.button_code_send);
                            sendButton.setEnabled(true);
                            sendButton.setTextColor(ContextCompat.getColor(DetailsActivity.this, R.color.white));
                            sendButton.invalidate();
                        }
                    };
                    timer.start();
                }
            }

            @Override
            public void onFailure(Call<Target> call, Throwable t) {

            }
        });
    }

}
