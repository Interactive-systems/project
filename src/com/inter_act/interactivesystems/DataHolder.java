package com.inter_act.interactivesystems;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;

public class DataHolder{

	private static DataHolder dataHolderObject;
	private Context context;
	private Activity activity;
	private Location mapLocation;
	private Location currentBestLocation;
	private ProgressDialog progressDialog;

	//======== Singleton Code =====================
	private DataHolder(){}
	public static DataHolder getDataHolderObject(){
		if (dataHolderObject == null){
			dataHolderObject = new DataHolder();
		}
		return dataHolderObject;
	}

	public void setContext(Context cxt){
		context = cxt;
		activity = (Activity) cxt;
	}

	public Context getContext() {
		return context;
	}

	public Location getMapLocation() {
		return mapLocation;
	}

	public void setMapLocation(Location location) {
		this.mapLocation = location;
	}

	public Location getCurrentBestLocation() {
		return currentBestLocation;
	}

	public void setCurrentBestLocation(Location currentBestLocation) {
		this.currentBestLocation = currentBestLocation;
	}
	
	public ProgressDialog getProgressDialog() {
		return progressDialog;
	}
	
	public void setProgressDialog(ProgressDialog pd) {
		this.progressDialog = pd;
	}
}