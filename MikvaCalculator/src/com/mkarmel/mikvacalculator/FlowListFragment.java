package com.mkarmel.mikvacalculator;

import java.util.ArrayList;
import java.util.Comparator;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class FlowListFragment extends ListFragment {
	private ArrayList<Flow> mFlows;
	public static final String PREVIOUS_FLOW = "previousflow12345";
	public static final String PREVIOUS_FLOW_AFTER_SUNSET = "previousflowaftersunset12345";
	private long mPreviousFlow;
	private boolean isPreviousFlowAfterSunset;
	private Comparator<Flow> flowComparator = new Comparator<Flow>() {

		@Override
		public int compare(Flow lhs, Flow rhs) {
			return rhs.getSawBlood().compareTo(lhs.getSawBlood());
		}
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		getActivity().setTitle(R.string.previous_flows);
		mFlows = FlowLab.get(getActivity()).getFlows();
		FlowAdapter adapter = new FlowAdapter(mFlows);
		adapter.sort(flowComparator);
		setListAdapter(adapter);
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if(mFlows.size() == 0){
			FragmentManager fm = getActivity().getSupportFragmentManager();
			WarningFragment dialog = new WarningFragment();
			dialog.setTargetFragment(FlowListFragment.this, 1);
			dialog.show(fm, "warning");
			return null;
		}else{
			View v = super.onCreateView(inflater, container, savedInstanceState);
			ListView listView = (ListView) v.findViewById(android.R.id.list);
			if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB){
				registerForContextMenu(listView);
			}else{
				listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
				listView.setMultiChoiceModeListener(new MultiChoiceModeListener() {
					
					@Override
					public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
						return false;
					}
					
					@Override
					public void onDestroyActionMode(ActionMode mode) {
						
					}
					
					@Override
					public boolean onCreateActionMode(ActionMode mode, Menu menu) {
						MenuInflater inflater = mode.getMenuInflater();
						inflater.inflate(R.menu.menu_delete_flow, menu);
						return true;
					}
					
					@Override
					public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
						switch(item.getItemId()){
							case R.id.menu_item_delete_flow:
								FlowAdapter adapter = (FlowAdapter) getListAdapter();
								FlowLab flowLab = FlowLab.get(getActivity());
								for(int i = adapter.getCount() - 1; i >= 0; i--){
									if(getListView().isItemChecked(i)){
										flowLab.deleteFlow(adapter.getItem(i));
									}
								}
								mode.finish();
								adapter.notifyDataSetChanged();
								return true;
							default:
								return false;
						}
					}
					
					@Override
					public void onItemCheckedStateChanged(ActionMode mode, int position,
							long id, boolean checked) {
						
					}
				});
			}
			
			return v;
		}
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		int pos = info.position;
		FlowAdapter adapter = (FlowAdapter)getListAdapter();
		Flow f = adapter.getItem(pos);
		switch(item.getItemId()){
			case R.id.menu_item_delete_flow:
				FlowLab.get(getActivity()).deleteFlow(f);
				adapter.notifyDataSetChanged();
				return true;
		}
		return super.onContextItemSelected(item);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		((FlowAdapter)getListAdapter()).notifyDataSetChanged();
		((FlowAdapter)getListAdapter()).sort(flowComparator);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
			case R.id.menu_item_add_flow:
				if(mFlows.size() > 0){
					if(mFlows.get(0) != null){
						mPreviousFlow = mFlows.get(0).getSawBlood().getTime();
						isPreviousFlowAfterSunset = !mFlows.get(0).isBeforeSunset();
					}else{
						mPreviousFlow = 0;
						isPreviousFlowAfterSunset = false;
					}
				}else{
					mPreviousFlow = 0;
					isPreviousFlowAfterSunset = false;
				}
				Intent i = new Intent(getActivity(), AddFlowActivity.class);
				i.putExtra(PREVIOUS_FLOW, mPreviousFlow);
				i.putExtra(PREVIOUS_FLOW_AFTER_SUNSET, isPreviousFlowAfterSunset);
				startActivity(i);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.menu_item_new_flow, menu);
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Flow f = ((FlowAdapter)getListAdapter()).getItem(position);
		Intent i = new Intent(getActivity(), FlowPagerActivity.class);
		i.putExtra(ViewFlowFragment.ID, f.getId());
		startActivity(i);
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		getActivity().getMenuInflater().inflate(R.menu.menu_delete_flow, menu);
	}
	
	private class FlowAdapter extends ArrayAdapter<Flow>{
		public FlowAdapter(ArrayList<Flow> flows){
			super(getActivity(),0,flows);
		}

		@SuppressLint("InflateParams")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView == null){
				convertView = getActivity().getLayoutInflater().
						inflate(android.R.layout.simple_list_item_1, null);
			}
			Flow f = getItem(position);
			TextView title = (TextView)convertView.findViewById(android.R.id.text1);
			title.setText(f.getSawBloodString());
			return convertView;
		}
	}
}

