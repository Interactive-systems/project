package com.inter_act.interactivesystems;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class MainActivity extends FragmentActivity implements LocationListener {

	GoogleMap googleMap;
	private LocationManager mlocManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);

		//Setting context to use in any environment
		DataHolder.getDataHolderObject().setContext(this);

		mlocManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
		mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);

		//Getting Google Play Services availability status
		int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());

		//Showing the status
		if (status != ConnectionResult.SUCCESS) { // Google Play Services are not available
			int requestCode = 10;
			Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
			dialog.show();
		} 
		else { //Google Play Services are available

			// Getting reference to the SupportMapFragment of map.xml
			SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

			googleMap = fm.getMap();
			googleMap.setMyLocationEnabled(true);
		}
	}

	public void locationChanged(Location location) {
		TextView tvLocation = (TextView) findViewById(R.id.tv_location);
		double latitude = location.getLatitude();
		double longitude = location.getLongitude();
		LatLng latLng = new LatLng(latitude, longitude);

		googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
		googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));

		tvLocation.setText("Latitude:" +  latitude  + ", Longitude:"+ longitude );
	}

	@Override
	public void onLocationChanged(Location location) {
		if (DataHolder.isBetterLocation(location, DataHolder.getDataHolderObject().getCurrentBestLocation())) {
			DataHolder.getDataHolderObject().setCurrentBestLocation(location);

			Log.d("test", "isBetterLocation");
		}
		else {
			location = DataHolder.getDataHolderObject().getCurrentBestLocation();
			Log.d("test", "isNotBetterLocation");
		}

		locationChanged(location);
	}

	@Override
	public void onProviderDisabled(String provider) {	
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	/*
	public void mapModeButtonClicked(View view) {	
		Log.d("test", "map mode button clicked");
		Intent mapMode = new Intent(MainActivity.this, CustomMapActivity.class);
		MainActivity.this.startActivity(mapMode);
	}
	 */

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.action_camera_mode:
			ProgressDialog pd = new ProgressDialog(this);
			pd.setTitle("Downloading image");
			pd.setCancelable(false);
			pd.setCanceledOnTouchOutside(false);
			pd.show();
			DataHolder.getDataHolderObject().setProgressDialog(pd);

			Intent cameraMode = new Intent(this, CameraPreviewActivity.class);
			cameraMode.putExtra("url", "http://upload.wikimedia.org/wikipedia/commons/thumb/9/9a/PNG_transparency_demonstration_2.png/300px-PNG_transparency_demonstration_2.png");
			cameraMode.putExtra("direction", "S");
			this.startActivity(cameraMode);
			break;
		case R.id.action_draw_mode:
			//Intent drawMode = new Intent(this, DrawActivity.class);
			//this.startActivity(drawMode);
			Log.d("test", "draw mode button clicked");
			break;
		case R.id.action_gallery_mode:
			//Intent galleryMode = new Intent(this, GalleryActivity.class);
			//this.startActivity(galleryMode);
			Log.d("test", "gallery mode button clicked");
			break;
		}
		return true;
	}
			
	public void onDestroy() {
		mlocManager.removeUpdates(this);
		super.onDestroy();
	}
} 