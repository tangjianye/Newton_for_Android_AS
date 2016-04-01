package com.fpliu.newton.framework.share;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.fpliu.newton.R;
import com.fpliu.newton.base.Environment;
import com.fpliu.newton.framework.ui.UIUtil;
import com.fpliu.newton.framework.ui.adapter.Item;
import com.fpliu.newton.framework.ui.dialog.CustomDialog;
import com.fpliu.newton.framework.ui.drawable.StateList;
import com.fpliu.newton.framework.util.BitmapUtil;
import com.tencent.stat.StatService;

/**
 * 分享弹出框
 * 
 * @author 792793182@qq.com 2015-02-04
 *
 */
public final class ShareDialog extends CustomDialog implements android.view.View.OnClickListener {

	/** 取消按钮的ID */
	public static final int ID_CANCEL_BTN = 0x6f0b0189;
	
	private ShareContent shareContent;
	
	/**
	 * 
	 * @param activity  
	 * @param rootView  Activity的根View，用于截图
	 */
	public ShareDialog(Activity activity, View rootView) {
		this(activity, rootView, new ShareContent());
	}
	
	/**
	 * 
	 * @param activity  
	 * @param rootView      Activity的根View，用于截图
	 * @param shareContent  分享内容
	 */
	public ShareDialog(Activity activity, View rootView, ShareContent shareContent) {
		super(activity);
		
		this.shareContent = shareContent;
		
		Bitmap snapshotBitmap = UIUtil.snapshot(rootView);
		String snapshotFilePath = Environment.getInstance().getMyDir() + "/share.jpg";
		boolean isSuccess = BitmapUtil.saveBitmapToFile(snapshotBitmap, snapshotFilePath, CompressFormat.JPEG);
		if (isSuccess) {
			snapshotBitmap.recycle();
			shareContent.addImage(snapshotFilePath);
		} else {
			snapshotFilePath = "";
		}
		
		//设置高度和宽度
		setWindowWidth(Environment.getInstance().getScreenWidth());
		
		int length = UIUtil.dip2px(activity, 15);
		
		LinearLayout layout = new LinearLayout(activity);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setBackgroundColor(Color.WHITE);
		setContentView(layout);
		
		TextView textView = new TextView(activity);
		textView.setText(R.string.third_party_share_to);
		textView.setTextSize(18);
		textView.setTextColor(Color.BLACK);
		textView.setPadding(length, length, 0, length);
		layout.addView(textView);
		
		Resources resources = activity.getResources();
		
		List<ShareItem> shareItems = new ArrayList<ShareDialog.ShareItem>();
		shareItems.add(new ShareItem(resources.getDrawable(R.drawable.ic_weixin_state),resources.getString(R.string.third_party_weixin)));
		shareItems.add(new ShareItem(resources.getDrawable(R.drawable.ic_qq_state), resources.getString(R.string.third_party_qq)));
		shareItems.add(new ShareItem(resources.getDrawable(R.drawable.ic_weibo_state), resources.getString(R.string.third_party_sina_weibo)));
		shareItems.add(new ShareItem(resources.getDrawable(R.drawable.ic_message_normal), resources.getString(R.string.third_party_mail)));
		shareItems.add(new ShareItem(resources.getDrawable(R.drawable.ic_timeline_state), resources.getString(R.string.third_party_timeline)));
		shareItems.add(new ShareItem(resources.getDrawable(R.drawable.ic_qqzone_state), resources.getString(R.string.third_party_qqzone)));
		shareItems.add(new ShareItem(resources.getDrawable(R.drawable.ic_message_state), resources.getString(R.string.third_party_message)));
		shareItems.add(new ShareItem(resources.getDrawable(R.drawable.ic_more_state), resources.getString(R.string.third_party_more)));
		
		TableLayout tableLayout = new TableLayout(activity);
		tableLayout.setColumnStretchable(0, true);
		tableLayout.setColumnStretchable(1, true);
		tableLayout.setColumnStretchable(2, true);
		tableLayout.setColumnStretchable(3, true);
		
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		lp.bottomMargin = length;
		lp.leftMargin = length;
		lp.rightMargin = length;
		layout.addView(tableLayout, lp);
		
		LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp2.gravity = Gravity.CENTER;
		
		for (int i = 0; i < 2; i++) {
			TableRow tableRow = new TableRow(activity);
			tableRow.setGravity(Gravity.CENTER);
			tableRow.setPadding(0, 0, 0, length);
			tableLayout.addView(tableRow);
			
			for (int j = 0; j < 4; j++) {
				int position = 4 * i + j;
				ShareItem shareItem = shareItems.get(position);
				
				LinearLayout itemView = new LinearLayout(activity);
				itemView.setOrientation(LinearLayout.VERTICAL);
				itemView.setTag(position);
				itemView.setOnClickListener(this);
				
				tableRow.addView(itemView);
				
				ImageView imageView = new ImageView(activity);
				imageView.setBackgroundDrawable(shareItem.drawable);
				
				TextView textView2 = new TextView(activity);
				textView2.setText(shareItem.text);
				textView2.setTextSize(15);
				textView2.setTextColor(Color.BLACK);
				textView2.setPadding(0, 10, 0, 10);
				
				itemView.addView(imageView, lp2);
				itemView.addView(textView2, lp2);
			}
		}
		
		Button button = new Button(activity);
		button.setId(ID_CANCEL_BTN);
		button.setText(R.string.cancel);
		button.setTextSize(18);
		button.setTextColor(Color.WHITE);
		button.setPadding(0, length, 0, length);
		button.setMinHeight(UIUtil.dip2px(activity, 40));
		button.setBackgroundDrawable(StateList.get());
		button.setOnClickListener(this);
		
		LinearLayout.LayoutParams lp3 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		lp3.bottomMargin = length;
		lp3.leftMargin = length;
		lp3.rightMargin = length;
		layout.addView(button, lp3);
	}

	@Override
	public void onClick(View view) {
		if (ID_CANCEL_BTN == view.getId()) {
			dismiss();
		} else {
			dismiss();
			
			Activity activity = getActivity();
			
			String which = "";
			
			switch ((Integer) view.getTag()) {
			case 0:
				which = "weixin_friend";
				ShareUtils.shareToWXFriend(activity, shareContent);
				break;
			case 1:
				which = "qq_friend";
				ShareUtils.shareToQQFriend(activity, shareContent);
				break;
			case 2:
				which = "sina_weibo";
				ShareUtils.shareToXinLangWeibo(activity, shareContent);
				break;
			case 3:
				which = "email";
				ShareUtils.shareToEmail(activity, shareContent);
				break;
			case 4:
				which = "weixin_timeline";
				ShareUtils.shareToWXTimeLine(activity, shareContent);
				break;
			case 5:
				which = "qzone";
				ShareUtils.shareToQZone(activity, shareContent);
				break;
			case 6:
				which = "msg";
				ShareUtils.shareToMsg(activity, shareContent);
				break;
			case 7:
				which = "more";
				ShareUtils.shareMore(activity, shareContent);
				break;
			default:
				break;
			}
			
			//统计分享功能
			Properties properties = new Properties();
			properties.put("os_version", Environment.getInstance().getOSVersionName());
			properties.put("phone_model", Environment.getInstance().getPhoneModel());
			properties.put("which", which);
			properties.put("where", shareContent.getWhere());
			StatService.trackCustomKVEvent(getActivity(), "share", properties);
		}
	}
	
	private static final class ShareItem implements Item {
		private Drawable drawable;
		private String text;
		
		public ShareItem(Drawable drawable, String text) {
			this.drawable = drawable;
			this.text = text;
		}
	}
}
