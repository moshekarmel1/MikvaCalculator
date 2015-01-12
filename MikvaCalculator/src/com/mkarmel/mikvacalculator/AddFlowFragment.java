package com.mkarmel.mikvacalculator;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class AddFlowFragment extends Fragment {
	private static final int REQUEST_DATE = 0;
	protected static final String DIALOG_DATE = "date";
	private Flow mFlow;
	public static int calYear,calMonth,calDay;
    private TextView sawBloodTV,hefsekTaharaTV,mikvaNightTV,day30TV,day31TV,haflagaTV;
    private Button before;
	public static Calendar cal;
	private Date previousFlow;
	private boolean isPreviousAfterSunset;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		mFlow = new Flow(getActivity());
		cal = new GregorianCalendar();
        calYear = cal.get(Calendar.YEAR);
        calMonth = cal.get(Calendar.MONTH);
        calDay = cal.get(Calendar.DAY_OF_MONTH);
        long previous = getActivity().getIntent().getLongExtra(FlowListFragment.PREVIOUS_FLOW, 0);
        isPreviousAfterSunset = getActivity().getIntent().getBooleanExtra(FlowListFragment.PREVIOUS_FLOW_AFTER_SUNSET, false);
        Log.e("Mr. Mo", "isPreviousAfterSunset : " + isPreviousAfterSunset);
        if(previous != 0){
        	previousFlow = new Date(previous);
        }
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.add_flow, container, false);
		sawBloodTV = (TextView)v.findViewById(R.id.saw_blood_display);
		hefsekTaharaTV = (TextView)v.findViewById(R.id.hefsek_tahara_display);
        mikvaNightTV = (TextView)v.findViewById(R.id.mikva_night_display);
        day30TV = (TextView)v.findViewById(R.id.day_thirty_display);
        day31TV = (TextView)v.findViewById(R.id.day_thirty_one_display);
        haflagaTV = (TextView)v.findViewById(R.id.haflaga_display);
        before = (Button)v.findViewById(R.id.before);
        before.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				FragmentManager fm = getActivity().getSupportFragmentManager();
				DatePickerFragment dialog = new DatePickerFragment();
				dialog.setTargetFragment(AddFlowFragment.this, REQUEST_DATE);
				dialog.show(fm, DIALOG_DATE);
			}
		});
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
			if(NavUtils.getParentActivityName(getActivity()) != null){
				getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
			}
        }
		return v;
	}
	
	public void MikvaCalculation(int year, int month, int day){
        Calendar calendar = new GregorianCalendar(year, month, day);
        Calendar diffCal = new GregorianCalendar(year, month, day);
        mFlow.setSawBlood(calendar.getTime());
        if(mFlow.isBeforeSunset()){
	        day += 4;
	        calendar.set(year, month, day);
	        mFlow.setHefsekTahara(calendar.getTime());
	        day += 7;
	        calendar.set(year, month, day);
	        mFlow.setMikvaNight(calendar.getTime());
	        day += 18;
        }else{
        	day += 5;
	        calendar.set(year, month, day);
	        mFlow.setHefsekTahara(calendar.getTime());
	        day += 7;
	        calendar.set(year, month, day);
	        mFlow.setMikvaNight(calendar.getTime());
	        day += 17;
        }
        
        calendar.set(year, month, day);
        mFlow.setDay30(calendar.getTime());
        day += 1;
        calendar.set(year, month, day);
        mFlow.setDay31(calendar.getTime());
        int diffInDays = 0;
        if(previousFlow != null){
        	if(previousFlow.getTime() != 0){
	        	long diff = mFlow.getSawBlood().getTime() - previousFlow.getTime();
	            diffInDays = (int) (diff / (24 * 60 * 60 * 1000));
	            diffInDays = figureOutDiff(isPreviousAfterSunset, mFlow.isBeforeSunset(), diffInDays);
	            diffCal.add(Calendar.DATE, diffInDays);
	            mFlow.setHaflaga(diffCal.getTime());
	            diffInDays += 1; // add a day because the day itself counts as a day
	            mFlow.setHaflagaDiff(diffInDays);
        	}
        }
        //set the dates in the text views
        sawBloodTV.setText(mFlow.getSawBloodString());
        hefsekTaharaTV.setText(mFlow.getHefsekTaharaString());
        mikvaNightTV.setText(mFlow.getMikvaNightString());
        day30TV.setText(mFlow.getDay30String());
        day31TV.setText(mFlow.getDay31String());
        if(mFlow.getHaflaga() != null){
        	haflagaTV.setText(mFlow.getHaflagaString(diffInDays));
        }
    }
	
	public int figureOutDiff(boolean previousAfter, boolean currentBefore, int diff){
		if(!previousAfter && !currentBefore){
			diff += 1;
		}
		else if(previousAfter && currentBefore){
			diff -= 1;
		}
        return diff;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode != Activity.RESULT_OK) return;
		if(requestCode == REQUEST_DATE){
			Date date = (Date)data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
			boolean beforeSunset = data.getBooleanExtra(DatePickerFragment.EXTRA_SUNSET, false);
			mFlow.setBeforeSunset(beforeSunset);
			cal.setTime(date);
	        calYear = cal.get(Calendar.YEAR);
	        calMonth = cal.get(Calendar.MONTH);
	        calDay = cal.get(Calendar.DAY_OF_MONTH);
	        MikvaCalculation(calYear, calMonth, calDay);
		}
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
			case android.R.id.home:
				if(NavUtils.getParentActivityName(getActivity()) != null){
					NavUtils.navigateUpFromSameTask(getActivity());
				}
				return true;
			case R.id.menu_item_add_to_cal:
				if(mFlow.getSawBlood() != null){
					Intent i = new Intent(getActivity(), FlowPagerActivity.class);
					i.putExtra(ViewFlowFragment.ID, mFlow.getId());
					startActivity(i);
				}
				return true;
			default:
				return super.onOptionsItemSelected(item);	
		}
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.add_to_cal_menu, menu);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		if(mFlow.getSawBlood() != null){
			FlowLab.get(getActivity()).addFlow(mFlow);
			FlowLab.get(getActivity()).saveFlows();
			Toast.makeText(getActivity(), "Flow Saved.", Toast.LENGTH_LONG).show();
		}else{
			FlowLab.get(getActivity()).deleteFlow(mFlow);
			Toast.makeText(getActivity(), "Flow was not saved.", Toast.LENGTH_LONG).show();
		}
	}

}


