package com.inter_act.interactivesystems;

import android.app.Activity;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Toast;

public class CameraPreviewActivity extends Activity implements SensorEventListener, AsyncImageCallback {

	private SensorManager mSensorManager;
	private Sensor mAccelerometer, mField;
	private float[] mGravity;
	private float[] mMagnetic;
	private DrawOnCameraPreview mDraw;
	private CameraPreview mPreview;
	private String pictureDirection = null;
	private String url = null;
	private Bitmap bmp = null;

	/** Called when the activity is first created. */ 
	@Override 
	public void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);

		Bundle extras = getIntent().getExtras();

		if (extras != null) {
			pictureDirection = extras.getString("direction");
			url = extras.getString("url");

			Log.d("test", "direction "+ pictureDirection);
			Log.d("test", "url "+ url);

			//fetch the image
			new ImageLoader(url, this);

			Log.d("jee", "loading ohi");

			while (bmp == null) {
				Log.d("test", "bmp == null, sleeping");
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			if (bmp == null) {
				Toast.makeText(this, "Unfortunately, something went wrong when trying to start the activity.\n" +
						"Please make sure that the internet connection is working and try again.", Toast.LENGTH_LONG).show();
				finish();
			}

			mDraw = new DrawOnCameraPreview(this, pictureDirection, bmp);
		}

		//no parameters given so just launch the camera
		else {
			mDraw = new DrawOnCameraPreview(this);
		}

		requestWindowFeature(Window.FEATURE_NO_TITLE); 

		mPreview = new CameraPreview(this, mDraw);

		mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
		mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mField = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

		setContentView(mPreview); 
		addContentView(mDraw, new LayoutParams 
				(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	}

	protected void onResume() {
		super.onResume();
		mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
		mSensorManager.registerListener(this, mField, SensorManager.SENSOR_DELAY_UI);
	}

	protected void onPause() {
		super.onPause();
		mSensorManager.unregisterListener(this);
	}

	private void updateDirection() {
		float[] temp = new float[9];
		float[] R = new float[9];

		SensorManager.getRotationMatrix(temp, null, mGravity, mMagnetic);		
		SensorManager.remapCoordinateSystem(temp, SensorManager.AXIS_X, SensorManager.AXIS_Z, R);

		float[] values = new float[3];
		SensorManager.getOrientation(R, values);

		//Convert to degrees
		for (int i=0; i < values.length; i++) {
			Double degrees = (values[i] * 180) / Math.PI;
			values[i] = degrees.floatValue();
		}

		mDraw.setDegrees(values[0]);
		mDraw.setDirection(getDirection(values[0]));
		mDraw.invalidate();
	}

	private String getDirection(float degrees) {
		if (degrees >= -22.5 && degrees < 22.5) { 
			return "N"; 
		}
		else if (degrees >= 22.5 && degrees < 67.5) { 
			return "NE"; 
		}
		else if (degrees >= 67.5 && degrees < 112.5) { 
			return "E"; 
		}
		else if (degrees >= 112.5 && degrees < 157.5) { 
			return "SE"; 
		}
		else if (degrees >= 157.5 || degrees < -157.5) { 
			return "S"; 
		}
		else if (degrees >= -157.5 && degrees < -112.5) { 
			return "SW"; 
		}
		else if (degrees >= -112.5 && degrees < -67.5) { 
			return "W"; 
		}
		else if (degrees >= -67.5 && degrees < -22.5) { 
			return "NW"; 
		}

		return null;
	}

	public static boolean isDirectionCloseEnough(String dir, String pictureDir) {
		if (pictureDir.equals("N")) {
			if (dir.equals("NE") || dir.equals("N") || dir.equals("NW")) {
				return true;
			}
		}
		else if (pictureDir.equals("E")) {
			if (dir.equals("NE") || dir.equals("E") || dir.equals("SE")) {
				return true;
			}
		}
		else if (pictureDir.equals("S")) {
			if (dir.equals("SE") || dir.equals("S") || dir.equals("SWE")) {
				return true;
			}
		}
		else if (pictureDir.equals("W")) {
			if (dir.equals("SW") || dir.equals("W") || dir.equals("NW")) {
				return true;
			}
		}

		else if (pictureDir.equals("NE")) {
			if (dir.equals("N") || dir.equals("NE") || dir.equals("E")) {
				return true;
			}
		}
		else if (pictureDir.equals("SE")) {
			if (dir.equals("E") || dir.equals("SE") || dir.equals("S")) {
				return true;
			}
		}
		else if (pictureDir.equals("SW")) {
			if (dir.equals("S") || dir.equals("SW") || dir.equals("W")) {
				return true;
			}
		}
		else if (pictureDir.equals("NW")) {
			if (dir.equals("W") || dir.equals("NW") || dir.equals("N")) {
				return true;
			}
		}

		return true;
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) { 
	}

	public void onSensorChanged(SensorEvent event) {
		switch(event.sensor.getType()) {
		case Sensor.TYPE_ACCELEROMETER:
			mGravity = event.values.clone();
			break;
		case Sensor.TYPE_MAGNETIC_FIELD:
			mMagnetic = event.values.clone();
			break;
		default:
			return;
		}

		if (mGravity != null && mMagnetic != null) {
			updateDirection();
		}
	}

	public void onImageReceived(String url, Bitmap bitmap) {
		Log.d("jee", "onImageReceived");
		if (bitmap == null) {
			this.finish();
		}
		else if (this.url.equals(url)) {
			bmp = bitmap;
		}
	}	
} 