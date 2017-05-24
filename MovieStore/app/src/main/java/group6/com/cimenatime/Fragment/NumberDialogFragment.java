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
 * Created by HauDT on 05/02/2017.
 */

public class NumberDialogFragment extends DialogFragment {

    private static final int MIN_YEAR = 1;
    private static final int MAX_YEAR = 10;
    private NumberPicker mNumberPicker;

    public static int number;

    public interface ShowNumberDialogListener {
        void sendNumber(int number);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.page_numberpicker, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        mNumberPicker = (NumberPicker) view.findViewById(R.id.pageNumberPicker);
        mNumberPicker.setMinValue(MIN_YEAR);
        mNumberPicker.setMaxValue(MAX_YEAR);
        mNumberPicker.setValue(number);

        mNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

                if (Number.mListener != null) {
                    Number.mListener.sendNumber(mNumberPicker.getValue());
                }

            }
        });
        return view;
    }

    public static class Number {
        public static ShowNumberDialogListener mListener;
    }
}
