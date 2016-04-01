package com.fpliu.newton.business.account;

import java.util.ArrayList;
import java.util.List;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fpliu.newton.R;
import com.fpliu.newton.base.Environment;
import com.fpliu.newton.business.account.RequestModifyUserInfo.What;
import com.fpliu.newton.business.account.login.LoginType;
import com.fpliu.newton.framework.net.RequestCallback;
import com.fpliu.newton.framework.net.RequestStatus;
import com.fpliu.newton.framework.ui.RoundListView;
import com.fpliu.newton.framework.ui.adapter.Item;
import com.fpliu.newton.framework.ui.adapter.ItemAdapter;
import com.fpliu.newton.framework.ui.adapter.ViewHolder;
import com.fpliu.newton.framework.ui.choosecity.CityDialog;
import com.fpliu.newton.framework.ui.choosecity.CityDialog.OnSelectedListenner;
import com.fpliu.newton.framework.ui.fragment.BaseFragment;
import com.fpliu.newton.framework.util.Helper;

/**
 * 账号设置界面
 * 
 * @author 792793182@qq.com 2014-10-15
 *
 */
public class AccountSettingFragment extends BaseFragment implements OnItemClickListener {

	private static final int POSITION_MODIFY_NICK = 0;
	private static final int POSITION_MODIFY_CITY = 1;
	private static final int POSITION_BIND_WITH_DRAW = 2;
	private static final int POSITION_BINGDING_PHONE_NUMBER = 3;
	private static final int POSITION_MODIFY_PASSWORD = 4;
	
	private RoundListView listview;
	
	@Override
	public RelativeLayout onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		RelativeLayout rootView = super.onCreateView(inflater, container, savedInstanceState);
		addContentView(inflater.inflate(R.layout.account_message, rootView, false));
		return rootView;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		setTitleText(Helper.getResourcesString(getActivity(), R.string.account_setting));
		
		listview = (RoundListView) view.findViewById(R.id.individual_center_listview);
		AccountSettingAdapter adapter = new AccountSettingAdapter(null);
		adapter.setData(UserManager.getInstance().getUserInfo());
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(this);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		requestUserInfo();
	}

	@Override
	protected void onFragmentResult(Bundle bundle) {
		super.onFragmentResult(bundle);
	
		requestUserInfo();
	}
	
	private void requestUserInfo() {
		if (!Environment.getInstance().isNetworkAvailable()) {
			return;
		}
		//请求用户信息
		RequestUserInfo.requestUserInfo(new RequestCallback<UserInfo>() {
			
			@Override
			public void callback(UserInfo userInfo, RequestStatus status) {
				if (status.getHttpStatusCode() == 200) {
					postMessage(MSG_REQUEST_USER_INFO_SUCCESS, userInfo);
				} else {
					String text = Helper.getResourcesString(getActivity(), R.string.get_account_failure);
					String discription = status.getHttpDescription();
					if (!TextUtils.isEmpty(discription)) {
						text = text + " : " + discription;
					}
				}
			}
		});
	}
	
	/** 请求用户信息成功后的消息 */
	private static final int MSG_REQUEST_USER_INFO_SUCCESS = 10;
	
	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case MSG_REQUEST_USER_INFO_SUCCESS:
			//如果界面没有关闭，就刷新界面
			if (!isFinished()) {
				AccountSettingAdapter adapter = (AccountSettingAdapter) listview.getAdapter();
				adapter.setData((UserInfo) msg.obj);
				adapter.notifyDataSetChanged();
			}
			break;
		default:
			break;
		}
		return super.handleMessage(msg);
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		switch (position) {
		case POSITION_MODIFY_NICK:
			addFragment(new ModifyNickNameFragment());
			break;
		case POSITION_MODIFY_CITY:
			if (!Environment.getInstance().isNetworkAvailable()) {
				Resources resource = getResources();
				String text = Helper.getResourcesString(getActivity(), R.string.net_disconnected);
				if(resource != null){
					showToast(text);
				}else{
					showToast(text);
				}
				return;
			}
			
			CityDialog cityDialog = new CityDialog(getActivity());
			cityDialog.setOnSelectedListenner(new OnSelectedListenner() {
				
				@Override
				public void onSelected(String city) {
					//请求修改城市
					RequestModifyUserInfo.requestModifyUserInfo(What.location, city, new RequestCallback<String>() {
						
						@Override
						public void callback(String result, RequestStatus status) {
							if (status.getHttpStatusCode() == 200) {
								//重新请求用户信息
								requestUserInfo();
							} else {
								String text = Helper.getResourcesString(getActivity(), R.string.modify_city_failure);
								
								String discription = status.getHttpDescription();
								if (!TextUtils.isEmpty(discription)) {
									text = text + " : " + discription;
								}
								postShowToast(text);
							}
						}
					});
				}
			});
			cityDialog.show(Gravity.CENTER, 0, 0);
			break;
		case POSITION_BIND_WITH_DRAW:
			
			break;
		case POSITION_BINGDING_PHONE_NUMBER:
			addFragment(new BindPhoneNumberFragment());
			break;
		case POSITION_MODIFY_PASSWORD:
			addFragment(new ModifyPasswordFragment());
			break;
		default:
			break;
		}
	}
	
	private class AccountSettingAdapter extends ItemAdapter<AccountSettingItem> {
		
		public AccountSettingAdapter(List<AccountSettingItem> items) {
			super(items);
		}

		private void setData(UserInfo userInfo) {
			if (userInfo == null) {
				return;
			}
			String my_nikename = Helper.getResourcesString(getActivity(), R.string.my_nickname);
			String current_city = Helper.getResourcesString(getActivity(), R.string.current_city);
			String withdraw_account = Helper.getResourcesString(getActivity(), R.string.withdraw_account);
			String bind_phoneNum = Helper.getResourcesString(getActivity(), R.string.bind_phoneNum);
			String modify_password = Helper.getResourcesString(getActivity(), R.string.modify_password);
			
			List<AccountSettingItem> items = new ArrayList<AccountSettingItem>();
			items.add(new AccountSettingItem(my_nikename, getLegal(userInfo.getNickName())));
			items.add(new AccountSettingItem(current_city, getLegal(userInfo.getCity())));
			items.add(new AccountSettingItem(withdraw_account+":", getLegal(userInfo.getAlipayNumber())));
			items.add(new AccountSettingItem(bind_phoneNum, getLegal(userInfo.getPhoneNum())));
			
			LoginType loginType = UserManager.getInstance().getLogin().getLoginResult().getLoginType();
			if (LoginType.Newton == loginType) {
				items.add(new AccountSettingItem(modify_password, ""));
			}
			
			setItems(items);
		}
		
		/**
		 * 获取一个合法的值
		 */
		private String getLegal(String value) {
			String unknow = Helper.getResourcesString(getActivity(), R.string.accountSettingFragment_unknow);
			if (TextUtils.isEmpty(value) || "null".equals(value)) {
				return unknow;
			}
			return value;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			AccountSettingItem item = getItem(position);
			
			ViewHolder viewHolder = ViewHolder.getViewHolder(position, R.layout.account_message_item, convertView, parent);
			
			TextView nameTV = viewHolder.getWidgetView(R.id.account_message_name);
			nameTV.setText(item.name);
			
			TextView valueTV = viewHolder.getWidgetView(R.id.account_message_key);
			valueTV.setText(item.value);
			
			return viewHolder.getConvertView();
		}
	}
	
	private static final class AccountSettingItem implements Item {
		private String name;
		private String value;
		
		private AccountSettingItem(String name, String value) {
			this.name = name;
			this.value = value;
		}
	}
}
