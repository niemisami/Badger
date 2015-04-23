package niemisami.badger;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


/**
 *
 */
public class DatePickerFragment extends DialogFragment {

    private final String TAG = "DatePickerFragment";
    public static final String EXTRA_DATE = "niemisami.badger.date";
    private Date mDate;


    public DatePickerFragment() {}


//    with newInstance only one instance of the DatePickerFragment can be created
//    By saving the date to the fragments arguments, it can be accessed from any class withing the application
    public static DatePickerFragment newInstance(Date date) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_DATE, date);

        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);

        return fragment;
    }


//    onCreateDialog initializes the calendar or the date picker
    @Override
    public Dialog onCreateDialog(Bundle savedStateInstance) {

        View v = getActivity().getLayoutInflater().inflate(R.layout.date_dialog, null);
        mDate = (Date)getArguments().getSerializable(EXTRA_DATE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mDate);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePicker datePicker = (DatePicker)v.findViewById(R.id.dialog_datePicker);
        datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int month, int day){
                mDate = new GregorianCalendar(year,month, day).getTime();
                getArguments().putSerializable(EXTRA_DATE, mDate);
            }
        });

        AlertDialog alertDialogDatePicker =new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle("Merkin päivä")
                .setPositiveButton(
                        android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sendResult(Activity.RESULT_OK);
                            }
                        })
                .create();

        return alertDialogDatePicker;
    }

    private void sendResult(int resultCode){
        if(getTargetFragment() == null){
            return;
        }
        Intent i = new Intent();
        i.putExtra(EXTRA_DATE, mDate);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, i);
    }


}