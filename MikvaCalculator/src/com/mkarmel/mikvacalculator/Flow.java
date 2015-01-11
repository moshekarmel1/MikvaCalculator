package com.mkarmel.mikvacalculator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

public class Flow {
	private static final String JSON_ID = "id";
	private static final String JSON_SAW_BLOOD = "sawblood";
	private static final String JSON_HEFSEK_TAHARA = "hefsektahara";
	private static final String JSON_MIKVA_NIGHT = "mikvanight";
	private static final String JSON_DAY_30 = "day30";
	private static final String JSON_DAY_31 = "day31";
	private static final String JSON_BEFORE_SUNSET = "beforesunset";
	private static final String JSON_HAFLAGA = "haflaga";
	private static final String JSON_HAFLAGA_DIFF = "haflagadiff";
	private Date mSawBlood,mHefsekTahara,mMikvaNight,mDay30,mDay31,mHaflaga;
	private UUID mId;
	private boolean mBeforeSunset;
	private Context mContext;
	private int mHaflagaDiff = 0;
	private String beforeSunsetString = " before sunset";
	private String afterSunsetString = " after sunset";
	private String dayString = "days";
	public boolean isBeforeSunset() {
		return mBeforeSunset;
	}

	public void setBeforeSunset(boolean beforeSunset) {
		mBeforeSunset = beforeSunset;
	}

	public Flow(){
		mId = UUID.randomUUID();
	}
	
	public Flow(Context ctx){
		mId = UUID.randomUUID();
		mContext = ctx;
		beforeSunsetString = mContext.getString(R.string.before_sunset);
		afterSunsetString = mContext.getString(R.string.after_sunset);
		dayString = mContext.getString(R.string.days);
	}
	
	public Flow(JSONObject json)throws JSONException{
		mId = UUID.fromString(json.getString(JSON_ID));
		mSawBlood = new Date(json.getLong(JSON_SAW_BLOOD));
		mHefsekTahara = new Date(json.getLong(JSON_HEFSEK_TAHARA));
		mMikvaNight = new Date(json.getLong(JSON_MIKVA_NIGHT));
		mDay30 = new Date(json.getLong(JSON_DAY_30));
		mDay31 = new Date(json.getLong(JSON_DAY_31));
		mBeforeSunset = json.getBoolean(JSON_BEFORE_SUNSET);
		if(json.has(JSON_HAFLAGA)){
			mHaflaga = new Date(json.getLong(JSON_HAFLAGA));
			if(json.has(JSON_HAFLAGA_DIFF)) mHaflagaDiff = json.getInt(JSON_HAFLAGA_DIFF);
		}
	}
	public JSONObject toJSON() throws JSONException{
		JSONObject json = new JSONObject();
		json.put(JSON_ID, mId.toString());
		json.put(JSON_SAW_BLOOD, mSawBlood.getTime());
		json.put(JSON_HEFSEK_TAHARA, mHefsekTahara.getTime());
		json.put(JSON_MIKVA_NIGHT, mMikvaNight.getTime());
		json.put(JSON_DAY_30, mDay30.getTime());
		json.put(JSON_DAY_31, mDay31.getTime());
		json.put(JSON_BEFORE_SUNSET, mBeforeSunset);
		if(mHaflaga != null){
			json.put(JSON_HAFLAGA, mHaflaga.getTime());
			json.put(JSON_HAFLAGA_DIFF, mHaflagaDiff);
		}
		return json;
	}
	@Override
	public String toString() {
		return mSawBlood.toString();
	}
	
	public Date getSawBlood() {
		return mSawBlood;
	}
	public void setSawBlood(Date sawBlood) {
		mSawBlood = sawBlood;
	}
	public Date getHefsekTahara() {
		return mHefsekTahara;
	}
	public void setHefsekTahara(Date hefsekTahara) {
		mHefsekTahara = hefsekTahara;
	}
	public Date getMikvaNight() {
		return mMikvaNight;
	}
	public void setMikvaNight(Date mikvaNight) {
		mMikvaNight = mikvaNight;
	}
	public Date getDay30() {
		return mDay30;
	}
	public void setDay30(Date day30) {
		mDay30 = day30;
	}
	public Date getDay31() {
		return mDay31;
	}
	public void setDay31(Date day31) {
		mDay31 = day31;
	}
	public UUID getId() {
		return mId;
	}
	public void setId(UUID id) {
		mId = id;
	}
	public Date getHaflaga() {
		return mHaflaga;
	}
	public int getHaflagaDiff(){
		return mHaflagaDiff;
	}
	public void setHaflagaDiff(int diff){
		mHaflagaDiff = diff;
	}
	public void setHaflaga(Date haflaga) {
		mHaflaga = haflaga;
	}
	public String getSawBloodString(){
		return formatDate(mSawBlood) + " " + ((mBeforeSunset) ? beforeSunsetString : afterSunsetString);
	}
	public String getHefsekTaharaString(){
		return formatDate(mHefsekTahara) + " " + beforeSunsetString;
	}
	public String getMikvaNightString(){
		return formatDate(mMikvaNight) + " " + afterSunsetString;
	}
	public String getDay30String(){
		return formatDate(mDay30) + " " + ((mBeforeSunset) ? beforeSunsetString : afterSunsetString);
	}
	public String getDay31String(){
		return formatDate(mDay31) + " " + ((mBeforeSunset) ? beforeSunsetString : afterSunsetString);
	}
	public String getHaflagaString(){
		return formatDate(mHaflaga) + " " + ((mBeforeSunset) ? beforeSunsetString : afterSunsetString);
	}
	public String getHaflagaString(int diffInDays){
		String formatted = formatDate(mHaflaga);
		String sunset = ((mBeforeSunset) ? beforeSunsetString : afterSunsetString);
		String days = String.valueOf(diffInDays);
		String returnString = formatted + " " + sunset;
		if(diffInDays != 0){
			returnString += " (";
			String daysStr = dayString;
			returnString += days + " " + daysStr + ")";
		}
		
		return returnString;
	}
	public String formatDate(Date date){
    	String str1 = "";
    	String str2 = "";
    	SimpleDateFormat sdf = new SimpleDateFormat("EEE",Locale.US);
    	DateFormat df = DateFormat.getDateInstance();
    	str1 = sdf.format(date);
    	str2 = df.format(date);
    	return str1 + " " + str2;
    }
}

