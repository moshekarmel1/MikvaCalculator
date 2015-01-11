package com.mkarmel.mikvacalculator;

import android.support.v4.app.Fragment;

public class AddFlowActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		return new AddFlowFragment();
	}

}
