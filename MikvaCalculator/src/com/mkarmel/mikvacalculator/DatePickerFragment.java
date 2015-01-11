package com.mkarmel.mikvacalculator;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;

public class DatePickerFragment extends DialogFragment {
	public static final String EXTRA_DATE = "com.mkarmel.mikvacalculator.date";
	public static final String EXTRA_SUNSET = "com.mkarmel.mikvacalculator.sunset";
	private Date mDate;
	private boolean mBeforeSunset;
	@SuppressLint("InflateParams")
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_date, null);
		DatePicker datePicker = (DatePicker) v.findViewById(R.id.dialog_date);
		mDate = new Date();
		Calendar cal = new GregorianCalendar();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		datePicker.init(year, month, day, new OnDateChangedListener() {
			
			@Override
			public void onDateChanged(DatePicker view, int yearOfYear, int monthOfYear,
					int dayOfMonth) {
				mDate = new GregorianCalendar(yearOfYear, monthOfYear, dayOfMonth).getTime();
			}
		});
		return new AlertDialog.Builder(getActivity())
			.setView(v).setIcon(R.drawable.ic_launcher)
			.setTitle("Choose the date of the flow")
			.setPositiveButton("After Sunset", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					mBeforeSunset = false;
					sendResult(Activity.RESULT_OK);
				}
			}).setNegativeButton("Before Sunset", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					mBeforeSunset = true;
					sendResult(Activity.RESULT_OK);
				}
			}).create();
	}
	
	private void sendResult(int resultCode){
		if(getTargetFragment() == null){
			return;
		}
		Intent i = new Intent();
		i.putExtra(EXTRA_DATE, mDate);
		i.putExtra(EXTRA_SUNSET, mBeforeSunset);
		getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, i);
	}

}

