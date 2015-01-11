package com.mkarmel.mikvacalculator;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;

@SuppressLint("InflateParams")
public class WarningFragment extends DialogFragment {
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		View v = getActivity().getLayoutInflater().inflate(R.layout.warning, null);
		return new AlertDialog.Builder(getActivity())
		.setView(v).setMessage(R.string.warning).setTitle(R.string.app_name).setIcon(R.drawable.ic_launcher)
		.setPositiveButton(android.R.string.ok, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent i = new Intent(getActivity(), AddFlowActivity.class);
				startActivity(i);
			}
		}).setNegativeButton(android.R.string.cancel, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				
			}
		}).create();
	}
}

