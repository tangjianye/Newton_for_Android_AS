package com.fpliu.newton.business.account.login;

import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.fpliu.newton.R;
import com.fpliu.newton.base.DebugLog;
import com.fpliu.newton.base.Environment;
import com.fpliu.newton.business.account.RequestCheckVerificationCode;
import com.fpliu.newton.business.account.RequestVerificationCode;
import com.fpliu.newton.framework.net.RequestCallback;
import com.fpliu.newton.framework.net.RequestStatus;
import com.fpliu.newton.framework.sms.SmsContent;
import com.fpliu.newton.framework.sms.SmsContentChangeCallback;
import com.fpliu.newton.framework.ui.drawable.StateList;
import com.fpliu.newton.framework.ui.fragment.BaseFragment;
import com.fpliu.newton.framework.util.StringUtil;
import com.tencent.stat.StatService;

/**
 * 注册的第一个页面
 * 
 * @author 792793182@qq.com 2014-10-15
 *
 */
@SuppressLint("ValidFragment")
public class RegisterFragment extends BaseFragment implements OnClickListener, 
                                                    SmsContentChangeCallback {
	
	private static final String TAG = RegisterFragment.class.getSimpleName();
	
	//电话号码输入框
	private EditText phoneNumberEdit;
	
	//验证码输入框
	private EditText verificationNumberEdit;
	
	//验证按钮
	private Button verificationBtn;
	
	private int count = 60;
	private Timer mTimer = new Timer();// 定时器
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		SmsContent smsContent = new SmsContent(getActivity(), this);
		// 注册短信变化监听
		getActivity().getContentResolver().registerContentObserver(
				Uri.parse("content://sms/"), true, smsContent);
		
		//杀死后进入的不算
		if (savedInstanceState == null) {
			//统计分享功能
			Properties properties = new Properties();
			properties.put("os_version", Environment.getInstance().getOSVersionName());
			properties.put("phone_model", Environment.getInstance().getPhoneModel());
			properties.put("action", "enter_first_page");
			StatService.trackCustomKVEvent(getActivity(), "register", properties);
		}
	}

	@Override
	public RelativeLayout onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		RelativeLayout rootView = super.onCreateView(inflater, container, savedInstanceState);
		addContentView(inflater.inflate(R.layout.user_register, rootView, false));
		return rootView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		setTitleText(R.string.register_btn);
		
		SharedPreferences share = getActivity().getSharedPreferences("user_register1", Activity.MODE_PRIVATE);
		String name = share.getString("myname", "");

		phoneNumberEdit = (EditText) view.findViewById(R.id.phone_num_user);
		phoneNumberEdit.setText(name);

		verificationNumberEdit = (EditText) view.findViewById(R.id.user_verification_user);
		
		verificationBtn = (Button) view.findViewById(R.id.get_verification_code_bt);
		verificationBtn.setOnClickListener(this);
		verificationBtn.setBackgroundDrawable(StateList.get());
		ImageButton next = (ImageButton) view.findViewById(R.id.user_register_bt2);
		next.setBackgroundDrawable(StateList.get());
		next.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		final String phoneNumber = phoneNumberEdit.getText().toString().trim();
		if (TextUtils.isEmpty(phoneNumber)) {
			showToast(R.string.bindPhoneNumberFragment_phoneNum_isEmpty);
			return;
		}
		
		if (!StringUtil.isPhomeNumber(phoneNumber)) {
			showToast(R.string.bindPhoneNumberFragment_phoneNum_error);
			return;
		}
		Environment environment = Environment.getInstance();
		if(!environment.isNetworkAvailable()){
			Resources resource = getResources();
			if(resource != null){
				showToast(resource.getString(R.string.net_disconnected));
			}else{
				showToast(R.string.net_disconnected);
			}
			return;
		}
		switch (v.getId()) {
		case R.id.user_register_bt2:
			final String verificationNumber = verificationNumberEdit.getText().toString().trim();
			if (TextUtils.isEmpty(verificationNumber)) {
				postShowToast(R.string.bindPhoneNumberFragment_security_code_isEmpty);
				return;
			}
			//先校验验证码
			RequestCheckVerificationCode.requestCheckVerificationCode(1, phoneNumber, verificationNumber, new RequestCallback<String>() {
				
				@Override
				public void callback(String result, RequestStatus status) {
					if (status.getHttpStatusCode() == 200) {
						postMessage(14, phoneNumber);
					} else {
						postShowToast(R.string.bindPhoneNumberFragment_security_code_error);
					}
				}
			});
			break;
		case R.id.get_verification_code_bt:
			count = 60;
			try {
				if (mTimer != null) {
					mTimer.cancel();// 退出之前的mTimer
				}
				mTimer = new Timer();// new一个Timer,否则会报错
				timerTask();
			} catch (IllegalStateException e) {
				DebugLog.e(TAG, "onClick()", e);
			}
			
			//请求验证码
			RequestVerificationCode.requestVerificationCode(1, phoneNumber, new RequestCallback<String>() {

				@Override
				public void callback(String result, RequestStatus status) {
					if (status.getHttpStatusCode() != 200) {
						postShowToast(status.getHttpDescription());
						postMessage(11, null);// 向Handler发送消息停止继续执行
					}
				}
			});
			break;
		default:
			break;
		}
	}

	private void timerTask() {
		// 创建定时线程执行更新任务
		mTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				if (count > 0) {
					postMessage(10, null);// 向Handler发送消息
				} else if (count == 0) {
					postMessage(11, null);// 向Handler发送消息停止继续执行
				} else {
					postMessage(11, null);// 向Handler发送消息停止继续执行
				}
				count--;
			}
		}, 1000, 1000);// 定时任务
	}

	@Override
	public void onDestroy() {
		if (mTimer != null) {
			mTimer.cancel();
		}
		super.onDestroy();
	}

	//短信内容自动添加
	@Override
	public void onChange(Object object) {
		if (verificationNumberEdit != null) {
			verificationNumberEdit.setText((String)object);
		}
	}
	
	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case 10:
			verificationBtn.setBackgroundColor(Color.GRAY);
			verificationBtn.setEnabled(false);
			verificationBtn.setText(count + "");
			break;
		case 11:
			if (mTimer != null) {
				mTimer.cancel();
				mTimer = null;
			}
			
			verificationBtn.setBackgroundResource(R.drawable.btn_bg_change);
			verificationBtn.setEnabled(true);
			verificationBtn.setText(R.string.get_security_code_again);
			break;
		case 14:
			Bundle bundle = new Bundle();
			bundle.putString(RegisterFragment2.KEY_PHONE_NUMBER, (String)msg.obj);
			
			RegisterFragment2 fragment = new RegisterFragment2();
			fragment.setArguments(bundle);
			
			addFragment(fragment);
			break;
		default:
			break;
		}
		return super.handleMessage(msg);
	}
}
