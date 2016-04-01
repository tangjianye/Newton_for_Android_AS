package com.fpliu.newton.business.setting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fpliu.newton.R;
import com.fpliu.newton.base.Environment;
import com.fpliu.newton.framework.ui.fragment.BaseFragment;

/**
 * 关于页面
 * 
 * @author 792793182@qq.com 2014-9-22
 *
 */
public final class AboutFragment extends BaseFragment {
	
	@Override
	public RelativeLayout onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		RelativeLayout rootView = super.onCreateView(inflater, container, savedInstanceState);
		addContentView(inflater.inflate(R.layout.aboutus, rootView, false));
		setTitleText(R.string.setting_about);
		return rootView;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		TextView textView = (TextView) view.findViewById(R.id.version);
		textView.setText("V" + Environment.getInstance().getMyVersionName());
	}
}
