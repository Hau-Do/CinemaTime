package group6.com.cimenatime.Fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by HauDT on 05/11/2017.
 */

public class DateDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    TextView txvSetDate;

    public DateDialogFragment(View view){
        txvSetDate = (TextView)view;
    }

    public DateDialogFragment() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        String data = dayOfMonth+"/"+monthOfYear+"/"+year;
        txvSetDate.setText(data);

    }
}
