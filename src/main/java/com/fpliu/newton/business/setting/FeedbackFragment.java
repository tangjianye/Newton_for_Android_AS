package com.fpliu.newton.business.setting;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.fpliu.newton.MyApp;
import com.fpliu.newton.R;
import com.fpliu.newton.base.Environment;
import com.fpliu.newton.business.setting.FeedBack.FeedBackCallback;
import com.fpliu.newton.framework.ui.UIUtil;
import com.fpliu.newton.framework.ui.fragment.BaseFragment;
import com.tencent.stat.StatService;

/**
 * 意见反馈页面
 * 
 * @author 792793182@qq.com 2014-9-22
 * 
 */
public final class FeedbackFragment extends BaseFragment implements OnClickListener, OnFocusChangeListener {

	/** 提交按钮的ID */
	private static final int ID_COMMIT_BTN = 0x5f0b0180;
	
	/** 建议内容编辑框 */
	private EditText suggestET;
	
	/** 用户的邮箱 */
	private EditText emailET;

	private Pattern p = Pattern.compile("\\s*|\t|\r|\n");

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (savedInstanceState == null) {
			//统计分析
			Properties properties = new Properties();
			properties.put("action", "enter");
			properties.put("os_version", Environment.getInstance().getOSVersionName());
			properties.put("phone_model", Environment.getInstance().getPhoneModel());
			StatService.trackCustomKVEvent(getActivity(), "feedback", properties);
		}
	}

	@Override
	public RelativeLayout onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		RelativeLayout rootView = super.onCreateView(inflater, container, savedInstanceState);
		addContentView(inflater.inflate(R.layout.suggest, rootView, false));
		
		Context context = getActivity();
		
		// 添加分组按钮
		Button commitBtn = new Button(context);
		commitBtn.setId(ID_COMMIT_BTN);
		commitBtn.setText(R.string.submit);
		commitBtn.setTextSize(18);
		commitBtn.setTextColor(Color.WHITE);
		commitBtn.setBackgroundResource(R.drawable.button_focused);
		commitBtn.setOnClickListener(this);
		
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		lp.addRule(RelativeLayout.CENTER_VERTICAL);
		addViewInTitleBar(commitBtn, lp);
		
		return rootView;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		setTitleText(R.string.setting_suggest);
		
		suggestET = (EditText) view.findViewById(R.id.editText);
		suggestET.setOnFocusChangeListener(this);
		
		emailET = (EditText) view.findViewById(R.id.suggest_connection);
		emailET.setOnFocusChangeListener(this);
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (!hasFocus) {
			UIUtil.hideSoftInput(getActivity(), v);
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case ID_COMMIT_BTN:
			final String suggestContent = suggestET.getText().toString().trim();

			Matcher m = p.matcher(suggestContent);
			if (m.matches()) {
				showToast(MyApp.getApp().getResources().getString(R.string.setting_suggest_information));
				return;
			}

			if (!Environment.getInstance().isNetworkAvailable()) {
				Resources resource = getResources();
				if(resource != null){
					showToast(resource.getString(R.string.net_disconnected));
				}else{
					showToast(R.string.net_disconnected);
				}
				return;
			}
			
			findViewById(ID_COMMIT_BTN).setEnabled(false);
			
			suggestET.clearFocus();
			emailET.clearFocus();
			
			String email = emailET.getText().toString();
			FeedBack.send(getActivity(), suggestContent, email, new FeedBackCallback() {
				
				@Override
				public void onFinish(int status) {
					new Handler(Looper.getMainLooper()).post(new Runnable() {
						
						@Override
						public void run() {
							showToast(MyApp.getApp().getResources().getString(R.string.setting_suggest_information_done));
							finish();
						}
					});
				}
			});
			break;
		}
	}
}
