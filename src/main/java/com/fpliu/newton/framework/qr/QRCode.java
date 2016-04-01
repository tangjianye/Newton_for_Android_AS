package com.fpliu.newton.framework.qr;

import java.io.FileNotFoundException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;

import com.google.zxing.Result;
import com.fpliu.newton.MyApp;
import com.fpliu.newton.R;
import com.fpliu.newton.base.DebugLog;
import com.fpliu.newton.framework.qr.activity.ResultDialog;
import com.fpliu.newton.framework.qr.decoding.DecodeHandler;
import com.fpliu.newton.framework.ui.toast.CustomToast;

/**
 * 二维码处理
 * 
 * @author 792793182@qq.com 2015-03-18
 *
 */
public final class QRCode {

	private static final String TAG = QRCode.class.getSimpleName();
	
	public static final int REQUST_CODE_QR_CODE = 9998;
	public static final int REQUST_CODE_PICK_IMAGE = 9999;
	
	private QRCode() {}
	
	public static void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
		DebugLog.d(TAG, "onActivityResult() data = " + data);
		
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == REQUST_CODE_QR_CODE) {
				Bundle bundle = data.getExtras();
				DebugLog.d(TAG, "bundle = " + bundle);
				
				if (bundle != null) {
					parser(activity, bundle.getString("result"));
				}
			} else if (requestCode == REQUST_CODE_PICK_IMAGE) {
				try {
					InputStream is = MyApp.getApp().getContentResolver().openInputStream(data.getData());
					Bitmap bitmap = BitmapFactory.decodeStream(is);
					
					Result result = DecodeHandler.decode(bitmap);
					DebugLog.d(TAG, "result = " + result);
					
					if (result == null) {
						CustomToast.makeText(activity, "此图片中不包含二维码", 3000).show();
					} else {
						String scanResult = result.getText();
						if (TextUtils.isEmpty(scanResult)) {
							CustomToast.makeText(activity, "此图片中不包含二维码", 3000).show();
						} else {
							parser(activity, scanResult);
						}
					}
				} catch (FileNotFoundException e) {
					DebugLog.e(TAG, "onActivityResult()", e);
					CustomToast.makeText(activity, R.string.image_select_images_not_exist, 3000).show();
				} catch (Exception e) {
					DebugLog.e(TAG, "onActivityResult()", e);
				} catch (Error e) {
					DebugLog.e(TAG, "onActivityResult()", e);
				}
			}
		}
	}
	
	/**
	 * 解析扫描结果
	 * @param scanResult
	 */
	private static void parser(Activity activity, String scanResult) {
		DebugLog.d(TAG, "scanResult = " + scanResult);
		
		Uri uri = Uri.parse(scanResult);
		
		String scheme = uri.getScheme();
		DebugLog.d(TAG, "scheme = " + scheme);
		
		String host = uri.getHost();
		DebugLog.d(TAG, "host = " + host);
		
		if ("newton".equals(scheme) && "newton.com".equals(host)) {
			
		} else {
			ResultDialog resultDialog = new ResultDialog(activity, scanResult);
			resultDialog.show(Gravity.CENTER, 0, 0, 0);
		}
	}
}
