package com.fpliu.newton.framework.bitmap;

import java.util.Date;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.RelativeLayout;

import com.fpliu.newton.R;
import com.fpliu.newton.base.DebugLog;
import com.fpliu.newton.base.Environment;
import com.fpliu.newton.base.camera.CameraManager;
import com.fpliu.newton.framework.ui.fragment.BaseFragment;
import com.fpliu.newton.framework.util.BitmapUtil;

/**
 * 拍照界面
 * 
 * @author 792793182@qq.com 2014-9-22
 *
 */
public final class TakePhotoFragment extends BaseFragment implements OnClickListener {

	private static final String TAG = TakePhotoFragment.class.getSimpleName();
	
	private CameraManager cameraManager = CameraManager.getInstance();
	
	
	@Override
	public RelativeLayout onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		RelativeLayout rootView = super.onCreateView(inflater, container, savedInstanceState);
		addContentView(inflater.inflate(R.layout.take_photo_fragment, container, false));
		return rootView;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		setTitleText("拍照");
		
		view.findViewById(R.id.take_photo_fragment_take_photo_btn).setOnClickListener(this);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		final SurfaceView surfaceView = (SurfaceView) findViewById(R.id.take_photo_fragment_surface_view);
		surfaceView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			
			@Override
			public void onGlobalLayout() {
				surfaceView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				cameraManager.initCamera(surfaceView, surfaceView.getWidth(), surfaceView.getHeight());
				DebugLog.d(TAG, "surfaceView.getWidth() = " + surfaceView.getWidth());
				DebugLog.d(TAG, "surfaceView.getHeight() = " + surfaceView.getHeight());
			}
		});
	}
	
	@Override
	public void onPause() {
		super.onPause();
		
		cameraManager.stopPreview();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		cameraManager.release();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.take_photo_fragment_take_photo_btn:
			cameraManager.takePhoto(new PictureCallback() {
				
				@Override
				public void onPictureTaken(byte[] data, Camera camera) {
					Bitmap bitmap = BitmapUtil.toBitmap(data);
					BitmapUtil.saveBitmapToFile(bitmap, Environment.getInstance().getMyDir() + "/" + new Date().getTime() + ".jpg", CompressFormat.JPEG);
					
					Bundle bundle = new Bundle();
					bundle.putParcelable("bitmap", bitmap);
					setResult(bundle);
					
					cameraManager.restartPreview();
				}
			});
			break;

		default:
			break;
		}
	}
}
