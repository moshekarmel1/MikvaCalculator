package com.mkarmel.mikvacalculator;

import java.util.ArrayList;
import java.util.UUID;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

public class FlowPagerActivity extends FragmentActivity {
	private ViewPager mViewPager;
	private ArrayList<Flow> mFlows;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mViewPager = new ViewPager(this);
		mViewPager.setId(R.id.viewPager);
		setContentView(mViewPager);
		mFlows = FlowLab.get(this).getFlows();
		FragmentManager fm = getSupportFragmentManager();
		mViewPager.setAdapter(new FragmentStatePagerAdapter(fm){
			
			@Override
			public int getCount() {
				return mFlows.size();
			}
			
			@Override
			public Fragment getItem(int pos) {
				Flow f = mFlows.get(pos);
				return ViewFlowFragment.newInstance(f.getId());
			}
		});
		
		mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int pos) {
				Flow f = mFlows.get(pos);
				setTitle(f.getSawBloodString());
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
		
		UUID flowId = (UUID) getIntent().getSerializableExtra(ViewFlowFragment.ID);
		for(int i = 0; i < mFlows.size(); i++){
			if(mFlows.get(i).getId().equals(flowId)){
				mViewPager.setCurrentItem(i);
				break;
			}
		}
	}
}

