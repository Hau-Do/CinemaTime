package group6.com.cimenatime.Fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.Date;

/**
 * Created by HauDT on 05/03/2017.
 */
public class SimpleDateTimePicker {

    private CharSequence mDialogTitle;
    private Date mInitDate;
    private DateTimeDialogFragment.OnDateTimeSetListener mOnDateTimeSetListener;
    private FragmentManager mFragmentManager;

    /**
     * Private constructor that can only be access using the make method
     * @param dialogTitle Title of the Date Time Picker Dialog
     * @param initDate Date object to use to set the initial Date and Time
     * @param onDateTimeSetListener OnDateTimeSetListener interface
     * @param fragmentManager Fragment Manager from the activity
     */
    private SimpleDateTimePicker(CharSequence dialogTitle, Date initDate,
                                 DateTimeDialogFragment.OnDateTimeSetListener onDateTimeSetListener,
                                 FragmentManager fragmentManager) {

        // Find if there are any DialogFragments from the FragmentManager
        FragmentTransaction mFragmentTransaction = fragmentManager.beginTransaction();
        Fragment mDateTimeDialogFrag = fragmentManager.findFragmentByTag(
                DateTimeDialogFragment.TAG_FRAG_DATE_TIME
        );

        // Remove it if found
        if(mDateTimeDialogFrag != null) {
            mFragmentTransaction.remove(mDateTimeDialogFrag);
        }
        mFragmentTransaction.addToBackStack(null);

        mDialogTitle = dialogTitle;
        mInitDate = initDate;
        mOnDateTimeSetListener = onDateTimeSetListener;
        mFragmentManager = fragmentManager;

    }

    /**
     * Creates a new instance of the SimpleDateTimePicker
     * @param dialogTitle Title of the Date Time Picker Dialog
     * @param initDate Date object to use to set the initial Date and Time
     * @param onDateTimeSetListener OnDateTimeSetListener interface
     * @param fragmentManager Fragment Manager from the activity
     * @return Returns a SimpleDateTimePicker object
     */
    public static SimpleDateTimePicker make(CharSequence dialogTitle, Date initDate,
                                            DateTimeDialogFragment.OnDateTimeSetListener onDateTimeSetListener,
                                            FragmentManager fragmentManager) {

        return new SimpleDateTimePicker(dialogTitle, initDate,
                onDateTimeSetListener, fragmentManager);

    }

    /**
     * Shows the DateTimePicker Dialog
     */
    public void show() {

        // Create new DateTimePicker
        DateTimeDialogFragment mDateTimePicker = DateTimeDialogFragment.newInstance(mDialogTitle,mInitDate);
        mDateTimePicker.setOnDateTimeSetListener(mOnDateTimeSetListener);
        mDateTimePicker.show(mFragmentManager, DateTimeDialogFragment.TAG_FRAG_DATE_TIME);

    }
}
