package com.inter_act.interactivesystems;

import android.graphics.Bitmap;

interface AsyncImageCallback {
	void onImageReceived(String url, Bitmap bm);
}