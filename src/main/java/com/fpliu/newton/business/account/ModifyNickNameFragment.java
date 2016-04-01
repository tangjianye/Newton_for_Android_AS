package com.fpliu.newton.business.account;

import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.fpliu.newton.R;
import com.fpliu.newton.base.Environment;
import com.fpliu.newton.business.account.RequestModifyUserInfo.What;
import com.fpliu.newton.framework.net.RequestCallback;
import com.fpliu.newton.framework.net.RequestStatus;
import com.fpliu.newton.framework.ui.drawable.StateList;
import com.fpliu.newton.framework.ui.fragment.BaseFragment;

/**
 * 修改昵称界面
 * 
 * @author 792793182@qq.com 2014-10-18
 * 
 */
public class ModifyNickNameFragment extends BaseFragment implements OnClickListener {

	/** 昵称输入框 */
	private EditText nameET;

	@Override
	public RelativeLayout onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		RelativeLayout rootView = super.onCreateView(inflater, container, savedInstanceState);
		addContentView(inflater.inflate(R.layout.change_username, rootView, false));
		return rootView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		setTitleText(R.string.modifyNickNameFragment_title);
		
		nameET = (EditText) findViewById(R.id.modify_name);

		Button modifyButton = (Button) findViewById(R.id.modify_button);
		modifyButton.setBackgroundDrawable(StateList.get());
		modifyButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		String nickName = nameET.getText().toString().trim();
		
		if (TextUtils.isEmpty(nickName)) {
			showToast(R.string.modifyNickNameFragment_nickname_isEntity);
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
		
		RequestModifyUserInfo.requestModifyUserInfo(What.nickName, nickName, new RequestCallback<String>() {
			
			@Override
			public void callback(String result, RequestStatus status) {
				if (status.getHttpStatusCode() == 200) {
					postFinish();
				} else {
					String text = getResources().getString(R.string.modifyPasswordFragment_fail);
					String discription = status.getHttpDescription();
					if (!TextUtils.isEmpty(discription)) {
						text = text + " : " + discription;
					}
					postShowToast(text);
				}
			}
		});
	}
}
