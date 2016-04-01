package com.fpliu.newton.business.setting;

import java.util.ArrayList;
import java.util.List;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fpliu.newton.R;
import com.fpliu.newton.base.DebugLog;
import com.fpliu.newton.base.Environment;
import com.fpliu.newton.base.ThreadPoolManager;
import com.fpliu.newton.business.config.UrlConfig;
import com.fpliu.newton.business.update.UpdateManager;
import com.fpliu.newton.business.update.UpdateResult;
import com.fpliu.newton.framework.net.HttpClientRequest;
import com.fpliu.newton.framework.net.RequestCallback;
import com.fpliu.newton.framework.net.RequestStatus;
import com.fpliu.newton.framework.ui.RoundListView;
import com.fpliu.newton.framework.ui.adapter.Item;
import com.fpliu.newton.framework.ui.adapter.ItemAdapter;
import com.fpliu.newton.framework.ui.adapter.ViewHolder;
import com.fpliu.newton.framework.ui.fragment.BaseFragment;

/**
 * 设置页面
 * 
 * @author 792793182@qq.com 2014-9-22
 * 
 */
public class SettingFragment extends BaseFragment implements OnItemClickListener {
	
	private static final String TAG = SettingFragment.class.getSimpleName();
	
	private int versionCodeOnServer;
	
	private static final int POSITION_HELP = 0;
	private static final int POSITION_FEEDBACK = 1;
	private static final int POSITION_ABOUT_US = 2;
	private static final int POSITION_VERSION = 3;

	private ArrayList<SettingItem> items;
	
	@Override
	public RelativeLayout onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		RelativeLayout rootView = super.onCreateView(inflater, container, savedInstanceState);
		addContentView(inflater.inflate(R.layout.settings_fragment, container, false));
		return rootView;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		setTitleText(R.string.setting_system);
		
		RoundListView listView = (RoundListView) view.findViewById(R.id.listview);
		listView.setOnItemClickListener(this);
		String help = getResources().getString(R.string.setting_help);
		String suggest = getResources().getString(R.string.setting_suggest);
		String about = getResources().getString(R.string.setting_about);
		String versions = getResources().getString(R.string.setting_versions);
		
		items = new ArrayList<SettingItem>();
		items.add(new SettingItem(help));
		items.add(new SettingItem(suggest));
		items.add(new SettingItem(about));
		items.add(new SettingItem(versions + Environment.getInstance().getMyVersionName()));

		listView.setAdapter(new SettingAdapter(items));
		
		//不停的取得数据
		ThreadPoolManager.EXECUTOR.execute(new Runnable() {
			
			@Override
			public void run() {
				while (!isFinished() && versionCodeOnServer == 0) {
					if (Environment.getInstance().isNetworkAvailable()) {
						DebugLog.d(TAG, "RequestVersionInfo()");
						
						HttpClientRequest.get(UrlConfig.UPDATE_VERSION, null, UpdateResult.class, new RequestCallback<UpdateResult>() {
							
							@Override
							public void callback(final UpdateResult updateResult, RequestStatus status) {
								DebugLog.d(TAG, "updateResult = " + updateResult);
								
								if (updateResult.getVersionCodeOnServer() > 0) {
									versionCodeOnServer = updateResult.getVersionCodeOnServer();
									// 等待10秒再去请求
								} else {
									try {
										Thread.sleep(10000);
									} catch (InterruptedException e) {
										DebugLog.e(TAG, "RequestVersionInfo()", e);
									}
								}
							}
						});
					} else {
						try {
							Thread.sleep(10000);
						} catch (InterruptedException e) {
							DebugLog.e(TAG, "RequestVersionInfo()", e);
						}
					}
				}
			}
		});
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		switch (position) {
		case POSITION_HELP:
			// 启动帮护界面
			addFragment(new HelpFragment());
			break;
		case POSITION_FEEDBACK:
			// 启动意见反馈界面
			addFragment(new FeedbackFragment());
			break;
		case POSITION_ABOUT_US:
			// 启动意见反馈界面
			addFragment(new AboutFragment());
			break;
		case POSITION_VERSION:
			if (!Environment.getInstance().isNetworkAvailable()) {
				Resources resource = getResources();
				if(resource != null){
					showToast(resource.getString(R.string.net_disconnected));
				} else{
					showToast(R.string.net_disconnected);
				}
				return;
			}
			
			// 当点击此条目的时候，已经请求过服务器了
			UpdateManager.showUpdateImmediate(getActivity(), versionCodeOnServer);
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
			SettingItem item = getItem(position);
			ViewHolder viewHolder = ViewHolder.getViewHolder(position, R.layout.settings_fragment_item, convertView, parent);
			TextView nameTv = (TextView) viewHolder.getWidgetView(R.id.settings_item);
			nameTv.setText(item.text);

			return viewHolder.getConvertView();
		}
	}

	private static final class SettingItem implements Item {
		private String text;

		private SettingItem(String text) {
			this.text = text;
		}
	}
}
