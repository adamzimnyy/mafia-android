package mafia.adamzimny.mafia.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mafia.adamzimny.mafia.R;
import mafia.adamzimny.mafia.util.helper.IntentHelper;


public class WelcomeActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.button_login)
    public void loginActivity(){
        IntentHelper.startActivityIntent(this,LoginActivity.class);
    }


    @OnClick(R.id.button_register)
    public void registerActivity(){
        IntentHelper.startActivityIntent(this,RegisterActivity.class);
    }
}
