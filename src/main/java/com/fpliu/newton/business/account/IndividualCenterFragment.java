package com.fpliu.newton.business.account;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import com.fpliu.newton.R;
import com.fpliu.newton.base.DebugLog;
import com.fpliu.newton.base.Environment;
import com.fpliu.newton.framework.designpattern.Observable;
import com.fpliu.newton.framework.designpattern.Observer;
import com.fpliu.newton.framework.net.RequestCallback;
import com.fpliu.newton.framework.net.RequestStatus;
import com.fpliu.newton.framework.push.PushManager;
import com.fpliu.newton.framework.push.PushMessage;
import com.fpliu.newton.framework.ui.RoundListView;
import com.fpliu.newton.framework.ui.UIUtil;
import com.fpliu.newton.framework.ui.adapter.Item;
import com.fpliu.newton.framework.ui.adapter.ItemAdapter;
import com.fpliu.newton.framework.ui.adapter.ViewHolder;
import com.fpliu.newton.framework.ui.dialog.alert.SweetAlertDialog;
import com.fpliu.newton.framework.ui.fragment.BaseFragment;
import com.fpliu.newton.framework.util.TestSetting;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * 个人中心界面
 * 
 * @author 792793182@qq.com 2014-10-15
 * 
 */
public class IndividualCenterFragment extends BaseFragment implements
		OnItemClickListener, OnClickListener, Observer<PushMessage> {

	private static final String TAG = IndividualCenterFragment.class.getSimpleName();

	private static final int ID_LOGOUT_BTN = 0x5f0b0180;
	
	private static final int MSG_REQUEST_USERINFO_FINISHED = 10;

	private static final int POSITION_RANK = 0;
	private static final int POSITION_BALANCE = 1;
	private static final int POSITION_TASK = 2;
	private static final int POSITION_MESSAGE = 3;
	
	/** 分享按钮 */
	private ImageButton logoutBtn;

	/** 头像图标 */
	private ImageView imageView;

	/** 昵称 */
	private TextView nickNameText;

	/** 等级 */
	private TextView levelText;

	/** 城市 */
	private TextView locationText;

	private RoundListView listview;

	private ArrayList<SettingItem> items;
	private SettingAdapter mAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// 注册观察者
		PushManager.getInstance().registerObserver(this);
	}
	
	@Override
	public RelativeLayout onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		RelativeLayout rootView = super.onCreateView(inflater, container, savedInstanceState);
		addContentView(inflater.inflate(R.layout.individual_center, rootView, false));
		return rootView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		setTitleText(R.string.individualCenterFragment_title);
		
		nickNameText = (TextView) view.findViewById(R.id.individual_nickname_tv);
		levelText = (TextView) view.findViewById(R.id.individual_level_tv);
		locationText = (TextView) view.findViewById(R.id.individual_location_tv);
		view.findViewById(R.id.individual_message).setOnClickListener(this);

		imageView = (ImageView) view.findViewById(R.id.avatar_image);
		imageView.setOnClickListener(this);

		Context context = getActivity();

		// 注销按钮
		logoutBtn = new ImageButton(context);
		logoutBtn.setId(ID_LOGOUT_BTN);
		logoutBtn.setImageResource(R.drawable.ic_logout);
		logoutBtn.setBackgroundResource(R.drawable.button_focused);
		logoutBtn.setOnClickListener(this);

		RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(
				UIUtil.dip2px(context, 40), LayoutParams.MATCH_PARENT);
		lp2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		lp2.addRule(RelativeLayout.CENTER_VERTICAL);
		addViewInTitleBar(logoutBtn, lp2);

		listview = (RoundListView) view
				.findViewById(R.id.individual_center_listview);
		listview.setOnItemClickListener(this);
		
		items = new ArrayList<SettingItem>();
		items.add(new SettingItem(POSITION_RANK, context.getResources()
				.getDrawable(R.drawable.ic_rank_black), getResources().getString(R.string.myIntegralFragment_title)));
		items.add(new SettingItem(POSITION_BALANCE, context.getResources()
				.getDrawable(R.drawable.ic_balance_black), getResources().getString(R.string.aboutus)));
		items.add(new SettingItem(POSITION_TASK, context.getResources()
				.getDrawable(R.drawable.ic_task_black), getResources().getString(R.string.myTaskFragment_title)));

		items.add(new SettingItem(POSITION_TASK, context.getResources()
				.getDrawable(R.drawable.ic_message), getResources().getString(R.string.myMessageFragment_title)));

		mAdapter = new SettingAdapter(items);
		listview.setAdapter(mAdapter);

		// 先读取缓存的数据，然后再更新
		UserInfo userInfo = UserManager.getInstance().getUserInfo();
		DebugLog.i(TAG, "Cached UserInfo = " + userInfo);

		attachUserInfo2View(userInfo);
	}

	@Override
	public void onResume() {
		super.onResume();

		requestUserInfo();
	}

	@Override
	protected void onFragmentResult(Bundle bundle) {
		super.onFragmentResult(bundle);

		DebugLog.v(TAG, " onFragmentResult");
		requestUserInfo();
	}

	private void requestUserInfo() {
		if (!Environment.getInstance().isNetworkAvailable()) {
			return;
		}
		// 请求用户信息
		RequestUserInfo.requestUserInfo(new RequestCallback<UserInfo>() {
			
			@Override
			public void callback(UserInfo userInfo, RequestStatus status) {
				postMessage(MSG_REQUEST_USERINFO_FINISHED, userInfo);
			}
		});
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		int count = listview.getChildCount();
		for (int i = 0; i < count; i++) {
			ViewGroup childView = (ViewGroup) listview.getChildAt(i);
			ImageView imageView = (ImageView) childView
					.findViewById(R.id.image_item);
			imageView.setImageDrawable(null);
			childView.removeAllViews();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.individual_message:
			addFragment(new AccountSettingFragment());
			break;
		case R.id.avatar_image:
			addFragment(new AvatarFragment());
			break;
		case ID_LOGOUT_BTN:
			new SweetAlertDialog(getActivity(), SweetAlertDialog.NORMAL_TYPE)
			.setTitleText(getResources().getString(R.string.individualCenterFragment_logout_content))
			.setCancelText(getResources().getString(R.string.individualCenterFragment_logout_ok))
			.setConfirmText(getResources().getString(R.string.individualCenterFragment_logout_cancel))
			.showCancelButton(true)
			.setCancelClickListener(
					new SweetAlertDialog.OnSweetClickListener() {
						@Override
						public void onClick(SweetAlertDialog sDialog) {
							sDialog.dismiss();
						}
					})
			.setConfirmClickListener(
					new SweetAlertDialog.OnSweetClickListener() {
						@Override
						public void onClick(SweetAlertDialog sDialog) {
							sDialog.dismiss();
							
							UserManager.getInstance().getLogin().logout(getActivity());
							
							TestSetting.TaskTestSettings.setRefreshAccesstoken(false);

							finish();
						}
					}).show();
			break;
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		switch (position) {
		case POSITION_RANK:
			if (!Environment.getInstance().isNetworkAvailable()) {
				Resources resource = getResources();
				if (resource != null) {
					showToast(resource.getString(R.string.net_disconnected));
				} else {
					showToast(R.string.net_disconnected);
				}
				return;
			}
			
			break;
		case POSITION_BALANCE:
			if (!Environment.getInstance().isNetworkAvailable()) {
				Resources resource = getResources();
				if (resource != null) {
					showToast(resource.getString(R.string.net_disconnected));
				} else {
					showToast(R.string.net_disconnected);
				}
				return;
			}
			
			break;
		case POSITION_TASK:
			if (!Environment.getInstance().isNetworkAvailable()) {
				Resources resource = getResources();
				if (resource != null) {
					showToast(resource.getString(R.string.net_disconnected));
				} else {
					showToast(R.string.net_disconnected);
				}
				return;
			}
			
			break;
		case POSITION_MESSAGE:
			// 检测网络连接
			if (!Environment.getInstance().isNetworkAvailable()) {
				Resources resource = getResources();
				if (resource != null) {
					showToast(resource.getString(R.string.net_disconnected));
				} else {
					showToast(R.string.net_disconnected);
				}
				return;
			}

			addFragment(new MyMessageFragment());
			break;
		default:
			break;
		}
	}

	private static final class SettingAdapter extends ItemAdapter<SettingItem> {

		private SettingAdapter(List<SettingItem> items) {
			super(items);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = ViewHolder.getViewHolder(position, R.layout.individual_center_item, convertView, parent);
			TextView nameTv = (TextView) viewHolder.getWidgetView(R.id.tv_item);
			ImageView imageView = (ImageView) viewHolder.getWidgetView(R.id.image_item);
			ImageView newMessageIv = (ImageView) viewHolder.getWidgetView(R.id.image_new);
			
			SettingItem item = getItem(position);

			nameTv.setText(item.text);
			imageView.setImageDrawable(item.icon);

			newMessageIv.setVisibility(View.GONE);
			// 判断是否有消息 有消息就显示new image
			int messageNumber = PushManager.getInstance().queryMessageNum();
			if (messageNumber != 0 && position == POSITION_MESSAGE) {
				DebugLog.v(TAG, "position" + position);

				newMessageIv.setVisibility(View.VISIBLE);
			}

			return viewHolder.getConvertView();
		}
	}

	private static final class SettingItem implements Item {
		private Drawable icon;
		private String text;

		private SettingItem(int position, Drawable icon, String text) {
			this.icon = icon;
			this.text = text;
		}
	}

	@Override
	public boolean handleMessage(Message msg) {
		if (isFinished()) {
			return super.handleMessage(msg);
		}

		switch (msg.what) {
		case MSG_REQUEST_USERINFO_FINISHED:
			UserInfo userInfo = (UserInfo) msg.obj;
			if (userInfo != null) {
				attachUserInfo2View(userInfo);
			} else {
				showToast(R.string.net_disconnected);
			}
		default:
			break;
		}
		return super.handleMessage(msg);
	}

	private void attachUserInfo2View(final UserInfo userInfo) {
		if (userInfo != null) {
			String avatar = userInfo.getAvatar();
			String nickName = userInfo.getNickName();
			String levelName = userInfo.getLevelName();
			String location = userInfo.getCity();

			if (location == null || "null".equals(location)) {
				locationText.setText(R.string.accountSettingFragment_unknow);
			} else {
				locationText.setText(location);
			}

			if (nickName != null && !"null".equals(nickName)) {
				nickNameText.setText(nickName);
			}

			if (levelName != null && !"null".equals(levelName)) {
				levelText.setText(levelName);
			}

			if (!TextUtils.isEmpty(avatar)) {
				int num = (int) (Math.random() * 1000);

				ImageLoader.getInstance().displayImage(avatar + "?num" + num,
						imageView, AvatarManager.getDisplayImageOptions(),
						new SimpleImageLoadingListener() {
							@Override
							public void onLoadingComplete(String imageUri,
									View view, Bitmap loadedImage) {
								// 图片加载完成后，保存
								DebugLog.i(TAG, "onLoadingComplete()");

								AvatarManager.saveToFile(loadedImage);
							}
						});
			}
		}
	}

	@Override
	public void update(Observable<PushMessage> observable, PushMessage... data) {
		DebugLog.v(TAG, "update()");
		
		if (items != null && mAdapter != null) {
			DebugLog.v(TAG, "notifyMessageChanged");
			
			mAdapter = new SettingAdapter(items);
			listview.setAdapter(mAdapter);
		}
	}
}
