package com.fpliu.newton.business.account.login;

import java.util.Properties;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TableRow.LayoutParams;

import com.fpliu.newton.R;
import com.fpliu.newton.base.Environment;
import com.fpliu.newton.business.account.IndividualCenterFragment;
import com.fpliu.newton.business.account.UserInfo;
import com.fpliu.newton.business.account.UserManager;
import com.fpliu.newton.framework.ui.UIUtil;
import com.fpliu.newton.framework.ui.drawable.StateList;
import com.fpliu.newton.framework.ui.fragment.BaseFragment;
import com.tencent.stat.StatService;

/**
 * 登录页面，包含QQ、微信、新浪微博第三方登录
 * 
 * @author 792793182@qq.com 2014-10-15
 *
 */
public final class LoginFragment extends BaseFragment implements OnClickListener {

	private static final int MSG_LOGIN_SUCCESS = 11;
	
	private static final int ID_REGISTER_BTN = 0x3f0b0189;
	
	private EditText userNameEdit;
	private EditText passwordEdit;

	private ILogin login;
	
	@Override
	public RelativeLayout onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		RelativeLayout rootView = super.onCreateView(inflater, container, savedInstanceState);
		addContentView(inflater.inflate(R.layout.login_all, rootView, false));
		return rootView;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		setTitleText(R.string.login_bt);
		
		Context context = getActivity();
		
		//注册按钮
		Button registerBtn = new Button(context);
		registerBtn.setId(ID_REGISTER_BTN);
		registerBtn.setText(R.string.register_btn);
		registerBtn.setTextSize(17);
		registerBtn.setTextColor(Color.WHITE);
		registerBtn.setBackgroundResource(R.drawable.button_focused);
		registerBtn.setOnClickListener(this);

		RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(
				UIUtil.dip2px(context, 60), LayoutParams.MATCH_PARENT);
		lp2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		lp2.addRule(RelativeLayout.CENTER_VERTICAL);
		addViewInTitleBar(registerBtn, lp2);
		
		//登录按钮
		Button loginBtn = (Button) view.findViewById(R.id.login_bt);
		loginBtn.setOnClickListener(this);
		loginBtn.setBackgroundDrawable(StateList.get());
				
		view.findViewById(R.id.qq_login_btn).setOnClickListener(this);
		view.findViewById(R.id.wechat_login_btn).setOnClickListener(this);
		view.findViewById(R.id.sina_weibo_login_btn).setOnClickListener(this);
		
		userNameEdit = (EditText) view.findViewById(R.id.username);
		passwordEdit = (EditText) view.findViewById(R.id.userpass);
		
		// 获取用户信息
		UserInfo userInfo = UserManager.getInstance().getUserInfo();
		if (userInfo != null) {
			userNameEdit.setText(userInfo.getUserName());
			passwordEdit.setText(userInfo.getPassword());
		}
		
		view.findViewById(R.id.forget_password_text).setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case ID_REGISTER_BTN:
			addFragment(new RegisterFragment());
			break;
		case R.id.forget_password_text:
			if (Environment.getInstance().isNetworkAvailable()) {
				
			} else {
				Resources resource = getResources();
				if(resource != null){
					showToast(resource.getString(R.string.net_disconnected));
				} else{
					showToast(R.string.net_disconnected);
				}
			}
			break;
		case R.id.login_bt:
			if (!Environment.getInstance().isNetworkAvailable()) {
				Resources resource = getResources();
				if(resource != null){
					showToast(resource.getString(R.string.net_disconnected));
				} else{
					showToast(R.string.net_disconnected);
				}
				return;
			}
			
			UIUtil.hideSoftInput(getActivity(), userNameEdit);
			UIUtil.hideSoftInput(getActivity(), passwordEdit);
			
			String userName = userNameEdit.getText().toString().trim();
			
			if (TextUtils.isEmpty(userName)) {
				showToast(R.string.empty_account);
				return;
			}
			
			String password = passwordEdit.getText().toString();
			
			if (TextUtils.isEmpty(password)) {
				showToast(R.string.empty_password);
				return;
			}
			
			Bundle params = new Bundle();
			params.putString("userName", userName);
			params.putString("password", password);
			
			login(LoginType.Newton, params);
			break;
		//QQ登录
		case R.id.qq_login_btn:
			login(LoginType.QQ, null);
			break;
		case R.id.wechat_login_btn:
			login(LoginType.WeiXin, null);
			break;
		case R.id.sina_weibo_login_btn:
			login(LoginType.SinaWeibo, null);
			break;
		}
	}

	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case MSG_LOGIN_SUCCESS:
			showToast(R.string.login_succeed);
			addFragment(new IndividualCenterFragment(), true);
		default:
			break;
		}
		return super.handleMessage(msg);
	}

	/** 
	 * 要想成功接收到腾讯开放平台SDK的接口的回调，必须实现此方法
	 * 参考：http://wiki.open.qq.com/wiki/%E5%88%9B%E5%BB%BA%E5%AE%9E%E4%BE%8B%E5%B9%B6%E5%AE%9E%E7%8E%B0%E5%9B%9E%E8%B0%83
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (login != null) {
			login.onActivityResult(requestCode, resultCode, data);
		}
	}
	
	private void login(final LoginType loginType, Bundle params) {
		if (!Environment.getInstance().isNetworkAvailable()) {
			Resources resource = getResources();
			if (resource != null){
				showToast(resource.getString(R.string.net_disconnected));
			} else{
				showToast(R.string.net_disconnected);
			}
			return;
		}
		
		login = LoginFactory.newInstance(loginType);
		
		UserManager.getInstance().setLogining(login);
		
		final Activity activity = getActivity();
		final Resources resources = activity.getResources();
		
		login.login(activity, params, new LoginCallback() {
			
			@Override
			public void onLoginSuccess(LoginResult loginResult) {
				UserManager.getInstance().setLogining(LoginFactory.newInstance(null));
				UserManager.getInstance().setLogin(login);
				
				postMessage(MSG_LOGIN_SUCCESS, loginResult);
				
				String which = "";
				switch (loginType) {
				case Newton:
					which = "newton";
					break;
				case WeiXin:
					which = "weixin";
					break;
				case QQ:
					which = "qq";
					break;
				case SinaWeibo:
					which = "sina_weibo";
					break;
				default:
					break;
				}
				//统计分享功能
				Properties properties = new Properties();
				properties.put("which", which);
				properties.put("os_version", Environment.getInstance().getOSVersionName());
				properties.put("phone_model", Environment.getInstance().getPhoneModel());
				StatService.trackCustomKVEvent(getActivity(), "login", properties);
			}
			
			@Override
			public void onLoginFail(LoginResult loginResult) {
				String error = "";
				if (TextUtils.isEmpty(error)) {
					error = resources.getString(R.string.login_fail);
				} else if ("access_denied".equals(error)) {
					error = resources.getString(R.string.login_account_not_exist);
				} else if ("validate_failure".equals(error)) {
					error = resources.getString(R.string.login_password_error);
				} else if ("server_error".equals(error)) {
					error = resources.getString(R.string.login_later);
				}
				
				postShowToast(error);
			}
		});
	}
}
