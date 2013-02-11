package com.inter_act.interactivesystems;

import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
	private SurfaceHolder mHolder;
	private Camera mCamera;
	private DrawOnCameraPreview mDraw;
	private boolean mFinished;

	CameraPreview(Context context, DrawOnCameraPreview draw) {
		super(context);

		mDraw = draw;
		mFinished = false;

		// Install a SurfaceHolder.Callback so we get notified when the
		// underlying surface is created and destroyed.
		mHolder = getHolder();
		mHolder.addCallback(this);
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	public void surfaceCreated(SurfaceHolder holder) {
		// The Surface has been created, now tell the camera where to draw the preview.
		try {
			mCamera = Camera.open();
			mCamera.setDisplayOrientation(90);
			mCamera.setPreviewDisplay(holder);
			mCamera.startPreview();

			// Preview callback used whenever new viewfinder frame is available
			mCamera.setPreviewCallback(new PreviewCallback() {
				public void onPreviewFrame(byte[] data, Camera camera) {

					if ((mDraw == null) || mFinished) {
						return;
					}	

					if (mDraw.getmBitmap() == null) {
						Camera.Parameters parameters = camera.getParameters();
						mDraw.setmImageWidth(parameters.getPreviewSize().width);
						mDraw.setmImageHeight(parameters.getPreviewSize().height);
						mDraw.setmBitmap(Bitmap.createBitmap(mDraw.getmImageWidth(), 
								mDraw.getmImageHeight(), Bitmap.Config.RGB_565));
						mDraw.setmYUVData(new byte[data.length]);

					}

					System.arraycopy(data, 0, mDraw.getmYUVData(), 0, data.length);
					mDraw.invalidate();
				}
			});
		} 
		catch (IOException exception) {
			mCamera.release();
			mCamera = null;
		}
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		mFinished = true;
		mCamera.setPreviewCallback(null);
		mCamera.stopPreview();
		mCamera.release();
		mCamera = null;
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		Camera.Parameters parameters = mCamera.getParameters();

		Size pSize = mCamera.getParameters().getPreviewSize();

		parameters.setPreviewSize(pSize.width, pSize.height);
		parameters.setPreviewFrameRate(15);
		parameters.setSceneMode(Camera.Parameters.SCENE_MODE_NIGHT);
		parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
		parameters.set("orientation", "portrait");
		parameters.set("rotation", 90);
		mCamera.setParameters(parameters);
		mCamera.startPreview();
	}
}