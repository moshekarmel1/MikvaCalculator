package com.mkarmel.mikvacalculator;

import android.support.v4.app.Fragment;

public class FlowListActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		return new FlowListFragment();
	}

}
