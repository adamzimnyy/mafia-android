package mafia.adamzimny.mafia.fragment.register;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;
import mafia.adamzimny.mafia.R;
import mafia.adamzimny.mafia.fragment.RegisterFragment;

import java.util.Calendar;


public class PersonalInfoFragment extends RegisterFragment {

    @BindView(R.id.first_name_field)
    AutoCompleteTextView firstNameField;

    @BindView(R.id.last_name_field)
    AutoCompleteTextView lastNameField;

    public PersonalInfoFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.personal_info_step, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    public String getFirstName() {

        String name = firstNameField.getText().toString();
        if (name.isEmpty())
            return "";

        String cap = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();

        return cap;
    }

    public String getLastName() {
        String name = lastNameField.getText().toString();
        if (name.isEmpty())
            return "";
        String cap = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
        return cap;
    }

    @Override
    public void getTemplateInfo() {
        getUserTemplate().setFirstName(getFirstName());
        getUserTemplate().setLastName(getLastName());
    }

    public AutoCompleteTextView getFirstNameField() {
        return firstNameField;
    }

    public AutoCompleteTextView getLastNameField() {
        return lastNameField;
    }
}