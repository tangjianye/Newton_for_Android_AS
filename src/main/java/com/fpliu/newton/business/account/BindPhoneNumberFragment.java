package com.fpliu.newton.business.account;

import java.util.Timer;
import java.util.TimerTask;

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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fpliu.newton.R;
import com.fpliu.newton.base.Environment;
import com.fpliu.newton.framework.net.RequestCallback;
import com.fpliu.newton.framework.net.RequestStatus;
import com.fpliu.newton.framework.sms.SmsContent;
import com.fpliu.newton.framework.sms.SmsContentChangeCallback;
import com.fpliu.newton.framework.ui.drawable.StateList;
import com.fpliu.newton.framework.ui.fragment.BaseFragment;
import com.fpliu.newton.framework.util.StringUtil;

/**
 * 绑定手机号界面
 * 
 * @author 792793182@qq.com 2014-10-20
 * 
 */
public final class BindPhoneNumberFragment extends BaseFragment implements
		OnClickListener, SmsContentChangeCallback {

	/** 更新数字的消息 */
	private static final int MSG_REFRESH_COUNT = 10;

	/** 请求验证码完成的消息 */
	private static final int MSG_REQUEST_VERIFICATION_CODE_FINISHED = 11;

	private TextView phoneNumTv, verification;
	private Button bingdingBt, verificationBt;
	private int count = 60;
	private Timer mTimer = new Timer();// 定时器

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		SmsContent smsContent = new SmsContent(getActivity(), this);
		// 注册短信变化监听
		getActivity().getContentResolver().registerContentObserver(
				Uri.parse("content://sms/"), true, smsContent);
	}

	@Override
	public RelativeLayout onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		RelativeLayout rootView = super.onCreateView(inflater, container, savedInstanceState);
		addContentView(inflater.inflate(R.layout.binding_phonenumber, rootView, false));
		return rootView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		setTitleText(R.string.bindPhoneNumberFragment_bind_phoneum);

		phoneNumTv = (TextView) view.findViewById(R.id.binding_phonenumber);
		verification = (TextView) view
				.findViewById(R.id.bingding_verification_code);

		bingdingBt = (Button) view.findViewById(R.id.bingding_phonenumber_bt);
		bingdingBt.setBackgroundDrawable(StateList.get());
		bingdingBt.setOnClickListener(this);

		verificationBt = (Button) view
				.findViewById(R.id.bingding_verification_code_bt);
		verificationBt.setBackgroundDrawable(StateList.get());
		verificationBt.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		final String phoneNumber = phoneNumTv.getText().toString().trim();
		if (TextUtils.isEmpty(phoneNumber)) {
			showToast(R.string.bindPhoneNumberFragment_phoneNum_isEmpty);
			return;
		}

		if (!StringUtil.isPhomeNumber(phoneNumber)) {
			showToast(R.string.bindPhoneNumberFragment_phoneNum_error);
			return;
		}

		if (!Environment.getInstance().isNetworkAvailable()) {
			Resources resource = getResources();
			if (resource != null) {
				showToast(R.string.net_disconnected);
			} else {
				showToast(R.string.net_disconnected);
			}
			return;
		}
		switch (view.getId()) {
		case R.id.bingding_verification_code_bt:
			isCanceled = false;
			// 从60开始倒数
			count = 60;

			// 按钮不可点击，并且置灰
			verificationBt.setEnabled(false);
			verificationBt.setBackgroundColor(Color.GRAY);
			verificationBt.setText(count + "");

			mTimer = new Timer();
			// 创建定时线程执行更新任务
			mTimer.schedule(new TimerTask() {
				@Override
				public void run() {
					if (count > 0) {
						count--;
						postMessage(MSG_REFRESH_COUNT, null);
					} else if (count == 0) {
						postMessage(MSG_REQUEST_VERIFICATION_CODE_FINISHED,
								null);
					}
				}
			}, 1000, 1000);// 定时任务

			// 请求验证码
			RequestVerificationCode.requestVerificationCode(2, phoneNumber, new RequestCallback<String>() {
				
				@Override
				public void callback(String result, RequestStatus status) {
					String text = getResources()
							.getString(
									R.string.bindPhoneNumberFragment_get_security_code_error);

					String discription = status.getHttpDescription();
					if (!TextUtils.isEmpty(discription)) {
						text = text + " : " + discription;
					}
					postShowToast(text);
					postMessage(MSG_REQUEST_VERIFICATION_CODE_FINISHED,
							null);
				}
			});
			break;
		case R.id.bingding_phonenumber_bt:
			String verificationCode = verification.getText().toString().trim();
			if (TextUtils.isEmpty(verificationCode)) {
				showToast(R.string.bindPhoneNumberFragment_security_code_isEmpty);
				return;
			}

			// 先验证验证码是否正确
			RequestCheckVerificationCode.requestCheckVerificationCode(2, phoneNumber, verificationCode, new RequestCallback<String>() {
				
				@Override
				public void callback(String result, RequestStatus status) {
					if (status.getHttpStatusCode() == 200) {
						UserInfo userInfo = UserManager.getInstance().getUserInfo();
						RequestBindPhoneNumber.requestBindPhoneNumber(phoneNumber, userInfo.getUserName(), new RequestCallback<String>() {
							
							@Override
							public void callback(String result, RequestStatus status) {
								if (status.getHttpStatusCode() == 200) {
									postFinish();
								} else {
									String text = getResources()
											.getString(
													R.string.bindPhoneNumberFragment_get_security_code_error);
									String discription = status.getHttpDescription();
									if (!TextUtils.isEmpty(discription)) {
										text = text + " : " + discription;
									}
									postShowToast(text);
								}
							}
						});
					} else {
						postShowToast(R.string.bindPhoneNumberFragment_security_code_error);
					}
				}
			});
			break;
		default:
			break;
		}
	}

	private boolean isCanceled = false;

	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case MSG_REFRESH_COUNT:
			if (!isCanceled) {
				verificationBt.setText(count + "");
			}
			break;
		case MSG_REQUEST_VERIFICATION_CODE_FINISHED:
			if (mTimer != null) {
				mTimer.cancel();
				mTimer = null;
			}

			isCanceled = true;

			verificationBt.setBackgroundResource(R.drawable.btn_bg_change);
			verificationBt.setEnabled(true);
			verificationBt.setText(R.string.get_security_code_again);
			break;
		default:
			break;
		}
		return super.handleMessage(msg);
	}

	// 短信内容自动添加
	@Override
	public void onChange(Object object) {
		if (verification != null) {
			verification.setText((String) object);
		}
	}
}
