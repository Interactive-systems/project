package com.inter_act.interactivesystems;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import android.graphics.BitmapFactory;

public class ImageLoader extends Thread {

	private String mUrl;
	private AsyncImageCallback mCallback;

	public ImageLoader(String urlString, AsyncImageCallback callback) {
		super();
		mUrl = urlString;
		mCallback = callback;
		start();
	}

	public void run() {
		try {
			HttpURLConnection connection = (HttpURLConnection)(new URL(mUrl)).openConnection();
			connection.setDoInput(true);
			connection.connect();
			mCallback.onImageReceived(mUrl, BitmapFactory.decodeStream(connection.getInputStream()));
		} catch (IOException e) {
			mCallback.onImageReceived(mUrl, null);
		}
	}
}