package com.fpliu.newton.business.account;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fpliu.newton.MyApp;
import com.fpliu.newton.R;
import com.fpliu.newton.base.DebugLog;
import com.fpliu.newton.base.ThreadPoolManager;
import com.fpliu.newton.business.config.SettingConfig;
import com.fpliu.newton.framework.net.RequestCallback;
import com.fpliu.newton.framework.net.RequestStatus;
import com.fpliu.newton.framework.ui.dialog.ImageChooseDialog;
import com.fpliu.newton.framework.ui.dialog.ImageChooseDialog.OnOpenCameraClick;
import com.fpliu.newton.framework.ui.dialog.ImageChooseDialog.OnPickImageClick;
import com.fpliu.newton.framework.ui.drawable.StateList;
import com.fpliu.newton.framework.ui.fragment.BaseFragment;
import com.fpliu.newton.framework.util.BitmapUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * 头像编辑界面
 * 
 * @author 792793182@qq.com 2014-10-17
 * 
 */
public class AvatarFragment extends BaseFragment implements OnClickListener {

	private static final String TAG = AvatarFragment.class.getSimpleName();

	private static final int TAKE_PICTURE = 0;
	private static final int CHOOSE_PICTURE = 1;
	private static final int CROP_FROM_CAMERA = 3;

	private static final int SCALE = 8;// 照片缩小比例

	/*** 上传头像结果 **/
	private static final int MSG_UPLOAD_PIC_FINISH = 0;

	private byte[] uploadImageByte;
	private Uri imageUri = null;

	/** 头像 */
	private ImageView icon;

	/** 上传头像按钮 */
	private Button uploadBtn;
	private ImageChooseDialog popup;

	@Override
	public RelativeLayout onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		RelativeLayout rootView = super.onCreateView(inflater, container, savedInstanceState);
		addContentView(inflater.inflate(R.layout.avatar, rootView, false));
		return rootView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		setTitleText(R.string.avatarFragment_avatar_edit);
		icon = (ImageView) view.findViewById(R.id.icon);
		String avatarPath = MyApp.getApp().getSetting().getString(SettingConfig.KEY_AVATAR_PATH, "");

		DebugLog.v(TAG, "avatarPath" + avatarPath);
		if (TextUtils.isEmpty(avatarPath)) {
			icon.setImageResource(R.drawable.ic_logo);
		} else {
			icon.setImageURI(Uri.fromFile(new File(avatarPath)));
		}
		uploadBtn = (Button) view.findViewById(R.id.upload_btn);
		uploadBtn.setBackgroundDrawable(StateList.get());
		uploadBtn.setText(R.string.avatarFragment_setting_avatar);
		uploadBtn.setOnClickListener(this);

		getActivity().getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			case TAKE_PICTURE:
				// 将保存在本地的图片取出并缩小后显示在界面上
				Bitmap bitmap = BitmapFactory.decodeFile(Environment
						.getExternalStorageDirectory() + "/image.jpg");
				Bitmap newBitmap = BitmapUtil.zoomBitmap(bitmap,
						bitmap.getWidth() / SCALE, bitmap.getHeight() / SCALE);
				// 由于Bitmap内存占用较大，这里需要回收内存，否则会报out of memory异常
				bitmap.recycle();
				doCrop();
				icon.setImageBitmap(newBitmap);
				uploadImageByte = BitmapUtil.compress(newBitmap,
						Bitmap.CompressFormat.JPEG);
				uploadBtn.setText(R.string.avatarFragment_upload_avatar);
				AvatarManager.saveToFile(newBitmap);
				break;
			case CHOOSE_PICTURE:
				// 照片的原始资源地址
				imageUri = data.getData();
				doCrop();
				try {
					ImageLoader.getInstance().displayImage(imageUri.toString(), icon, AvatarManager.getDisplayImageOptions(),
							new SimpleImageLoadingListener() {
								@Override
								public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

									DebugLog.i(TAG, "onLoadingComplete()");
									uploadImageByte = BitmapUtil.compress(loadedImage, Bitmap.CompressFormat.JPEG);

									uploadBtn.setText(R.string.avatarFragment_upload_avatar);
									AvatarManager.saveToFile(loadedImage);
								}
							});
					DebugLog.v(TAG, "oom is ok");
				} catch (Exception e) {
					DebugLog.e(TAG, "", e);
				}
				break;
			case CROP_FROM_CAMERA:
				if (null != data) {
					saveCutPic(data);
				}
				break;
			default:
				break;
			}
		}
	}

	private void doCrop() {
		DebugLog.v(TAG, "doCrop");
		final Context context = getActivity();
		PackageManager pm = context.getPackageManager();

		final ArrayList<CropOption> cropOptions = new ArrayList<CropOption>();
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setType("image/*");
		List<ResolveInfo> list = pm.queryIntentActivities(intent, 0);
		if (list.isEmpty()) {
			showToast("Can not find image crop app");
			return;
		} else {
			intent.setData(imageUri);
			intent.putExtra("outputX", 200);
			intent.putExtra("outputY", 200);
			intent.putExtra("aspectX", 1);
			intent.putExtra("aspectY", 1);
			intent.putExtra("scale", true);
			intent.putExtra("return-data", true);
			if (list.size() == 1) {
				Intent i = new Intent(intent);
				ResolveInfo res = list.get(0);
				i.setComponent(new ComponentName(res.activityInfo.packageName,
						res.activityInfo.name));
				startActivityForResult(i, CROP_FROM_CAMERA);
			} else {
				for (ResolveInfo res : list) {
					final CropOption co = new CropOption();

					co.title = pm
							.getApplicationLabel(res.activityInfo.applicationInfo);
					co.icon = pm
							.getApplicationIcon(res.activityInfo.applicationInfo);
					co.appIntent = new Intent(intent);
					co.appIntent
							.setComponent(new ComponentName(
									res.activityInfo.packageName,
									res.activityInfo.name));

					cropOptions.add(co);
				}
				CropOptionAdapter adapter = new CropOptionAdapter(context,
						cropOptions);
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle("Choose Crop App");
				builder.setAdapter(adapter,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int item) {
								startActivityForResult(
										cropOptions.get(item).appIntent,
										CROP_FROM_CAMERA);
							}
						});
				builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialog) {

						if (imageUri != null) {
							context.getContentResolver().delete(imageUri, null,
									null);
							imageUri = null;
						}
					}
				});
				AlertDialog alert = builder.create();
				alert.show();
			}
		}
	}

	private void showPop(final Activity activity) {
		if (popup == null) {
			popup = new ImageChooseDialog(activity);
			popup.setOnOpenCameraClick(new OnOpenCameraClick() {

				@Override
				public void onClick() {
					Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

					imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "image.jpg"));
					// 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
					openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
					startActivityForResult(openCameraIntent, 0);

					popup.dismiss();
				}
			});
			popup.setOnPickImageClick(new OnPickImageClick() {

				@Override
				public void onClick() {
					Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
					openAlbumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
					startActivityForResult(openAlbumIntent, 1);
					popup.dismiss();
				}
			});
		}
		popup.show(Gravity.CENTER, 0, 0, 0);
	}

	private void saveCutPic(Intent picdata) {
		Bundle bundle = picdata.getExtras();
		if (null != bundle) {
			Bitmap mBitmap = bundle.getParcelable("data");
			uploadImageByte = BitmapUtil.compress(mBitmap, Bitmap.CompressFormat.JPEG);
			icon.setImageBitmap(mBitmap);
		}
		File f = new File(imageUri.getPath());
		if (f.exists()) {
			f.delete();
		}
	}

	@Override
	public void onClick(View view) {
		if (view == uploadBtn) {
			Resources resource = getResources();
			
			if (String.valueOf(uploadBtn.getText()).equals(resource.getString(R.string.avatarFragment_setting_avatar))) {
				showPop(getActivity());
			} else if (String.valueOf(uploadBtn.getText()).equals(resource.getString(R.string.avatarFragment_upload_avatar))) {
				if (!com.fpliu.newton.base.Environment.getInstance().isNetworkAvailable()) {
					showToast(resource.getString(R.string.net_disconnected));
					return;
				}

				if (uploadImageByte != null) {
					showToast(R.string.avatarFragment_uploading_avatar);

					ThreadPoolManager.EXECUTOR.execute(new Runnable() {

						@Override
						public void run() {
							final String base64 = Base64.encodeToString(uploadImageByte, Base64.DEFAULT);

							UserInfo userInfo = UserManager.getInstance().getUserInfo();
							RequestModifyAvatar.requestModifyAvatar(base64, userInfo.getUserName(), new RequestCallback<String>() {

								@Override
								public void callback(String result,
										RequestStatus status) {
									if (status.getHttpStatusCode() == 200) {
										postMessage(MSG_UPLOAD_PIC_FINISH, true);
										postFinish();

									} else {
										postMessage(MSG_UPLOAD_PIC_FINISH, false);
										postFinish();
									}
								}
							});
						}
					});
				}
			}
		}
	}

	@Override
	public boolean handleMessage(Message msg) {
		if (isFinished()) {
			return super.handleMessage(msg);
		}

		switch (msg.what) {
		case MSG_UPLOAD_PIC_FINISH:
			if ((Boolean) msg.obj) {
				showToast(R.string.avatarFragment_upload_avatar_succeed);
			} else {
				showToast(R.string.avatarFragment_upload_avatar_fail);
			}
			break;
		default:
			break;
		}
		return super.handleMessage(msg);
	}

	
	static class CropOptionAdapter extends ArrayAdapter<CropOption> {
		private ArrayList<CropOption> mOptions;
		private LayoutInflater mInflater;
		
		public CropOptionAdapter(Context context, ArrayList<CropOption> options) {
			super(context, R.layout.crop_selector, options);
			
			mOptions 	= options;
			mInflater	= LayoutInflater.from(context);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.crop_selector, parent, false);
			}
			
			CropOption item = mOptions.get(position);
			
			if (item != null) {
				((ImageView) convertView.findViewById(R.id.iv_icon)).setImageDrawable(item.icon);
				((TextView) convertView.findViewById(R.id.tv_name)).setText(item.title);
				
				return convertView;
			}
			
			return null;
		}
	}
	
	static class CropOption {
		public CharSequence title;
		public Drawable icon;
		public Intent appIntent;
	}
}
