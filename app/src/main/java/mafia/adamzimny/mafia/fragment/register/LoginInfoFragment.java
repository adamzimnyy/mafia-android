package mafia.adamzimny.mafia.fragment.register;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mafia.adamzimny.mafia.R;
import mafia.adamzimny.mafia.fragment.RegisterFragment;

/**
 * Created by adamz on 19.08.2016.
 */
public class LoginInfoFragment extends RegisterFragment {

    @BindView(R.id.username_field)
    AutoCompleteTextView usernameField;

    @BindView(R.id.password_field)
    AutoCompleteTextView passwordField;

    @BindView(R.id.email_field)
    AutoCompleteTextView emailField;


    public LoginInfoFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_info_step, container, false);
        ButterKnife.bind(this, view);
        return view;
    }
public AutoCompleteTextView getPasswordField() {
        return passwordField;
    }
    public String getUsername() {
        return usernameField.getText().toString();

    }

    public String getPassword() {
        return passwordField.getText().toString();
    }

    public String getEmail() {
        return emailField.getText().toString();
    }

    @Override
    public void getTemplateInfo() {
        getUserTemplate().setUsername(getUsername());
        getUserTemplate().setEmail(getEmail());
        getUserTemplate().setPassword(getPassword());

    }

    public AutoCompleteTextView getUsernameField() {
        return usernameField;
    }


    public AutoCompleteTextView getEmailField() {
        return emailField;
    }
}