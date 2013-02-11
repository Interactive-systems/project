package com.inter_act.interactivesystems;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class DrawOnCameraPreview extends View { 

	private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private float degrees = -1;
	private String direction = "null";
	private String pictureDirection = "null";
	private Bitmap mBitmap;
	private byte[] mYUVData;
	private int mImageWidth, mImageHeight;
	private Bitmap drawing;

	public DrawOnCameraPreview(Context context) { 
		super(context);

		paint.setStyle(Paint.Style.FILL); 
		paint.setColor(Color.BLACK); 
		paint.setTextSize(30);

		mBitmap = null;
		mYUVData = null;
	} 

	public DrawOnCameraPreview(Context context, String pictureDirection, Bitmap bmp) {
		super(context);

		paint.setStyle(Paint.Style.FILL); 
		paint.setColor(Color.BLACK); 
		paint.setTextSize(30);

		mBitmap = null;
		mYUVData = null;

		this.pictureDirection = pictureDirection;
		this.drawing = bmp;
	}

	public Bitmap getmBitmap() {
		return mBitmap;
	}

	public void setmBitmap(Bitmap mBitmap) {
		this.mBitmap = mBitmap;
	}

	public int getmImageHeight() {
		return mImageHeight;
	}

	public void setmImageHeight(int mImageHeight) {
		this.mImageHeight = mImageHeight;
	}

	public byte[] getmYUVData() {
		return mYUVData;
	}

	public void setmYUVData(byte[] mYUVData) {
		this.mYUVData = mYUVData;
	}

	public int getmImageWidth() {
		return mImageWidth;
	}

	public void setmImageWidth(int mImageWidth) {
		this.mImageWidth = mImageWidth;
	}

	@Override 
	protected void onDraw(Canvas canvas) { 
		if (mBitmap != null && CameraPreviewActivity.isDirectionCloseEnough(direction, pictureDirection) && drawing != null) {
			canvas.drawBitmap(drawing, 100, 150, paint);
		}

		super.onDraw(canvas); 
	}

	public void setDegrees(float degrees) {
		this.degrees = degrees;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}
} 