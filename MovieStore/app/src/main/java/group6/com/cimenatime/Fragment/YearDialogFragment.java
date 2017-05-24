package group6.com.cimenatime.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.NumberPicker;

import group6.com.cimenatime.R;

/**
 * Created by HauDT on 05/04/2017.
 */
public class YearDialogFragment extends DialogFragment {

    private static final int MIN_YEAR = 1980;
    private static final int MAX_YEAR = 2020;
    private NumberPicker mNumberPicker;

    public static int year;

    public interface ShowYearDialogListener {
        void sendYear(int year);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.year_numberpicker, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        mNumberPicker = (NumberPicker) view.findViewById(R.id.yearNumberPicker);
        mNumberPicker.setMinValue(MIN_YEAR);
        mNumberPicker.setMaxValue(MAX_YEAR);
        mNumberPicker.setValue(year);

        mNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

                if (Year.mListener != null) {
                    Year.mListener.sendYear(mNumberPicker.getValue());
                }

            }
        });
        return view;
    }

    public static class Year {
        public static ShowYearDialogListener mListener;
    }

}
