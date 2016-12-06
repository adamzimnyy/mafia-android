package mafia.adamzimny.mafia.fragment.register;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mafia.adamzimny.mafia.R;
import mafia.adamzimny.mafia.fragment.RegisterFragment;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by adamz on 19.08.2016.
 */
public class BirthdayFragment extends RegisterFragment implements DatePicker.OnDateChangedListener {

    @BindView(R.id.date_picker)
    DatePicker datePicker;

    public BirthdayFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void initDatepicker() {
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        datePicker.init(mYear, mMonth, mDay, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.birthday_step, container, false);
        ButterKnife.bind(this, view);
        initDatepicker();
        return view;
    }

    @Override
    public void onDateChanged(DatePicker datePicker, int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        getUserTemplate().setDateOfBirth(cal.getTime());
    }

    public Date getBirthday() {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
        calendar.set(Calendar.MONTH, datePicker.getMonth() + 1);
        calendar.set(Calendar.YEAR, datePicker.getYear());
        return calendar.getTime();
    }

    @Override
    public void getTemplateInfo() {
        getUserTemplate().setDateOfBirth(getBirthday());
    }
}
