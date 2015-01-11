package com.mkarmel.mikvacalculator;

import java.util.Date;
import java.util.UUID;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ViewFlowFragment extends Fragment {
	public static final String ID = "ouqwetroueqewr";
	private Flow mFlow;
    private TextView sawBloodTV,hefsekTaharaTV,mikvaNightTV,day30TV,day31TV,haflagaTV;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		UUID flowId = (UUID)getArguments().getSerializable(ID);
		mFlow = FlowLab.get(getActivity()).getFlow(flowId);
	}
	
	public static ViewFlowFragment newInstance(UUID flowId){
		Bundle args = new Bundle();
		args.putSerializable(ID, flowId);
		ViewFlowFragment frag = new ViewFlowFragment();
		frag.setArguments(args);
		return frag;
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.view_flow, container, false);
		sawBloodTV = (TextView)v.findViewById(R.id.flow_saw_blood_display);
		sawBloodTV.setText(mFlow.getSawBloodString());
		sawBloodTV.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				addToCalendar(mFlow.getSawBlood(), getActivity().getString(R.string.saw_blood));
				
			}
		});
		hefsekTaharaTV = (TextView)v.findViewById(R.id.flow_hefsek_tahara_display);
		hefsekTaharaTV.setText(mFlow.getHefsekTaharaString());
		hefsekTaharaTV.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				addToCalendar(mFlow.getHefsekTahara(), getActivity().getString(R.string.hefsek));
				
			}
		});
        mikvaNightTV = (TextView)v.findViewById(R.id.flow_mikva_night_display);
        mikvaNightTV.setText(mFlow.getMikvaNightString());
        mikvaNightTV.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				addToCalendar(mFlow.getMikvaNight(), getActivity().getString(R.string.mikva));
				
			}
		});
        day30TV = (TextView)v.findViewById(R.id.flow_day_thirty_display);
        day30TV.setText(mFlow.getDay30String());
        day30TV.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				addToCalendar(mFlow.getDay30(), getActivity().getString(R.string.day_thirty));
				
			}
		});
        day31TV = (TextView)v.findViewById(R.id.flow_day_thirty_one_display);
        day31TV.setText(mFlow.getDay31String());
        day31TV.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				addToCalendar(mFlow.getDay31(), getActivity().getString(R.string.day_thirty_one));
				
			}
		});
        haflagaTV = (TextView)v.findViewById(R.id.haflaga_display_past);
        if(mFlow.getHaflaga() != null){
	        haflagaTV.setText(mFlow.getHaflagaString(mFlow.getHaflagaDiff()));
	        haflagaTV.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					addToCalendar(mFlow.getHaflaga(), getActivity().getString(R.string.haflaga));
					
				}
			});
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
        	if(NavUtils.getParentActivityName(getActivity()) != null){
        		getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        	}
        }
		return v;
	}
	
	public void addToCalendar(Date date, String title){
    	Intent intent = new Intent(Intent.ACTION_EDIT);
    	intent.setType("vnd.android.cursor.item/event");
    	intent.putExtra("beginTime", date);
    	intent.putExtra("allDay", true);
    	intent.putExtra("endTime", date);
    	intent.putExtra("title", title);
    	startActivity(intent);
    }
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.delete_single_flow, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
			case android.R.id.home:
				if(NavUtils.getParentActivityName(getActivity()) != null){
					NavUtils.navigateUpFromSameTask(getActivity());
				}
				return true;
			case R.id.delete_single_flow:
				FlowLab.get(getActivity()).deleteFlow(mFlow);
				Intent i = new Intent(getActivity(), FlowListActivity.class);
				startActivity(i);
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		FlowLab.get(getActivity()).saveFlows();
	}
}
