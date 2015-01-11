package com.mkarmel.mikvacalculator;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import android.content.Context;

public class FlowJSONSerializer {
	private Context mContext;
	private String mFilename;
	
	public FlowJSONSerializer(Context c, String s){
		mContext = c;
		mFilename = s;
	}
	
	public ArrayList<Flow> loadFlows() throws IOException, JSONException{
		ArrayList<Flow> flows = new ArrayList<Flow>();
		BufferedReader reader = null;
		try{
			InputStream in = mContext.openFileInput(mFilename);
			reader = new BufferedReader(new InputStreamReader(in));
			StringBuilder jsonString = new StringBuilder();
			String line = null;
			while((line = reader.readLine()) != null){
				jsonString.append(line);
			}
			JSONArray array = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();
			for(int i = 0; i < array.length(); i++){
				flows.add(new Flow(array.getJSONObject(i)));
			}
		}catch(FileNotFoundException e){
			
		}finally{
			if(reader != null)
				reader.close();
		}
		return flows;
	}
	
	public void saveFlows(ArrayList<Flow> flows) throws JSONException, IOException{
		JSONArray array = new JSONArray();
		for(Flow f : flows){
			array.put(f.toJSON());
		}
		Writer writer = null;
		try{
			OutputStream out = mContext.openFileOutput(mFilename, Context.MODE_PRIVATE);
			writer = new OutputStreamWriter(out);
			writer.write(array.toString());
		}finally{
			if(writer != null){
				writer.close();
			}
		}
	}
}

