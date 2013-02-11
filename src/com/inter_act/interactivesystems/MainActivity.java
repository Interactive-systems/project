package com.inter_act.interactivesystems;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//Setting context to use in any environment
		DataHolder.getDataHolderObject().setContext(this);
	}

	public void mapModeButtonClicked(View view) {
		Log.d("test", "map mode button clicked");
		Intent mapMode = new Intent(MainActivity.this, CustomMapActivity.class);
		MainActivity.this.startActivity(mapMode);
	}

	public void drawModeButtonClicked(View view) {
		//Intent drawMode = new Intent(this, DrawActivity.class);
		//this.startActivity(drawMode);
		Log.d("test", "draw mode button clicked");
	}

	public void galleryModeButtonClicked(View view) {
		//Intent galleryMode = new Intent(this, GalleryActivity.class);
		//this.startActivity(galleryMode);
		Log.d("test", "gallery mode button clicked");
	}
	
	public void cameraModeButtonClicked(View view) {
		Intent cameraMode = new Intent(this, CameraPreviewActivity.class);
		cameraMode.putExtra("url", "http://upload.wikimedia.org/wikipedia/commons/thumb/9/9a/PNG_transparency_demonstration_2.png/300px-PNG_transparency_demonstration_2.png");
		cameraMode.putExtra("direction", "S");
		this.startActivity(cameraMode);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
	}

} 