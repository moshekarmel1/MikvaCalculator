package com.mkarmel.mikvacalculator;

import java.util.ArrayList;
import java.util.UUID;

import android.content.Context;
import android.util.Log;

public class FlowLab {
	private static final String FILENAME = "mikvaflows.json";
	private ArrayList<Flow> mFlows;
	private static FlowLab sFlowLab;
	private Context mAppContext;
	private FlowJSONSerializer mSerializer;
	
	private FlowLab(Context appContext){
		mAppContext = appContext;
		mSerializer = new FlowJSONSerializer(mAppContext, FILENAME);
		try{
			mFlows = mSerializer.loadFlows();
		}catch(Exception e){
			mFlows = new ArrayList<Flow>();
		}
	}
	
	public boolean saveFlows(){
		try{
			mSerializer.saveFlows(mFlows);
			return true;
		}catch(Exception e){
			Log.e("TAG", "Problem saving the flows...", e);
			return false;
		}
	}
	
	public void deleteFlow(Flow f){
		mFlows.remove(f);
	}
	
	public void addFlow(Flow f){
		mFlows.add(f);
	}
	
	public static FlowLab get(Context c){
		if(sFlowLab == null){
			sFlowLab = new FlowLab(c.getApplicationContext());
		}
		return sFlowLab;
	}
	
	public ArrayList<Flow> getFlows(){
		return mFlows;
	}
	
	public Flow getFlow(UUID flowId){
		for(Flow f : mFlows){
			if(f.getId().equals(flowId)){
				return f;
			}
		}
		return null;
	}
}

