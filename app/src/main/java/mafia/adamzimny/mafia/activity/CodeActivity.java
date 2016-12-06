package mafia.adamzimny.mafia.activity;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.melih.holdtoload.HoldToLoadLayout;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import mafia.adamzimny.mafia.R;
import mafia.adamzimny.mafia.api.RetrofitBuilder;
import mafia.adamzimny.mafia.api.service.CodeService;
import mafia.adamzimny.mafia.constant.Locations;
import mafia.adamzimny.mafia.constant.Params;
import mafia.adamzimny.mafia.model.Code;
import mafia.adamzimny.mafia.model.Location;
import mafia.adamzimny.mafia.util.AppVariable;
import mafia.adamzimny.mafia.util.DateUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.Date;
import java.util.Random;

public class CodeActivity extends AppCompatActivity {
    static CountDownTimer timer;
    String codeString;
    boolean responseOk = false;
    boolean canSendRequests = true;
    public static final int TWO_MINUTES = 2 * 60 * 1000;
    @BindView(R.id.hold)
    HoldToLoadLayout button;

    @BindView(R.id.code)
    EditText codeField;

    @BindView(R.id.progress_bar)
    CircularProgressBar progressBar;

    @BindView(R.id.time)
    TextView time;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    float angle1 = 0, angle2 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        button.setFillListener(new HoldToLoadLayout.FillListener() {
            @Override
            public void onFull() {
                if (canSendRequests) {
                    generateCode();
                    codeField.setText(codeString);
                    progressBar.setProgress(0);
                    canSendRequests = false;
                }
                button.setStrokeColor(R.color.target_green);
                button.setDuration(750);
            }

            @Override
            public void onEmpty() {
                button.setStrokeColor(R.color.blue_accent);
                button.setDuration(1500);
                responseOk = !responseOk;
                canSendRequests = true;
            }

            @Override
            public void onAngleChanged(float v) {
                angle1 = angle2;
                angle2 = v;


            }
        });

        if (AppVariable.code != null) {
            timer.cancel();
            codeField.setText(AppVariable.code);
            time.setText(DateUtils.formatMillisAsYMDHMS(AppVariable.codeTimeLeft));
            progressBar.setProgress(AppVariable.codeTimeLeft * 100f / TWO_MINUTES);
            Log.d("progress", "progress = " + AppVariable.codeTimeLeft * 100f / TWO_MINUTES);

            Log.d("progress", "millis = " + AppVariable.codeTimeLeft);
            timer = new CountDownTimer(AppVariable.codeTimeLeft, 1000) {

                public void onTick(long millisUntilFinished) {
                    AppVariable.codeTimeLeft = millisUntilFinished;
                    time.setText(DateUtils.formatMillisAsYMDHMS(millisUntilFinished));
                    progressBar.setProgressWithAnimation(millisUntilFinished * 100f / TWO_MINUTES);
                    Log.d("progress", "progress = " + millisUntilFinished * 100f / TWO_MINUTES);
                }

                public void onFinish() {
                    time.setText("");
                    progressBar.setProgress(0);
                    codeField.setText("");
                }
            };
            timer.start();
        } else {
            codeField.setText("");
            progressBar.setProgress(0);
            time.setText("");
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void generateCode() {

        codeString = nextString(Params.CODE_LENGTH);
        AppVariable.code = codeString;
        Log.d("code", "Code generated! " + codeString);
        if (timer != null) timer.cancel();
        timer = new CountDownTimer(TWO_MINUTES, 1000) {

            public void onTick(long millisUntilFinished) {
                AppVariable.codeTimeLeft = millisUntilFinished;
                time.setText(DateUtils.formatMillisAsYMDHMS(millisUntilFinished));
                progressBar.setProgressWithAnimation(millisUntilFinished * 100f / TWO_MINUTES, 2000);
                Log.d("progress", "progress = " + millisUntilFinished * 100f / TWO_MINUTES);
            }

            public void onFinish() {
                codeField.setText("Code expired!");
                progressBar.setProgress(0);
                time.setText("0m 00s");
            }

        };
        timer.start();
        Location codeLocation = AppVariable.lastKnownLocation;
        codeLocation.setType(Locations.CODE);
        Code code = Code.Builder
                .create()
                .withCode(codeString)
                .withCreated(new Date())
                .withLocation(codeLocation)
                .withUser(AppVariable.loggedUser)
                .build();

        CodeService service = (CodeService) RetrofitBuilder.getService(CodeService.class, RetrofitBuilder.BASE_URL);
        Call<String> call = service.createNewCode(AppVariable.token, code);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.code() == 200) {
                    responseOk = !responseOk;
                    Log.d("code", "Response ok");
                    //     button.setStrokeColor(R.color.target_green);
                } else {
                    Log.d("code", "Response not ok");
                    //     button.setStrokeColor(R.color.target_red);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private final Random random = new Random();

    public String nextString(int len) {
        StringBuilder tmp;

        tmp = new StringBuilder();
        for (char ch = '0'; ch <= '9'; ++ch)
            tmp.append(ch);
        for (char ch = 'A'; ch <= 'Z'; ++ch)
            tmp.append(ch);
        char[] symbols = tmp.toString().toCharArray();
        tmp.setLength(0);

        for (int i = 0; i < len; i++)
            tmp.append(symbols[random.nextInt(symbols.length)]);
        return tmp.toString();
    }
}
