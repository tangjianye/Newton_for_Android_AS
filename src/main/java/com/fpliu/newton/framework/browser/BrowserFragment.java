package com.fpliu.newton.framework.browser;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.fpliu.newton.framework.ui.fragment.BaseFragment;

/**
 * 浏览器Fragment
 * 
 * @author 792793182@qq.com 2014-9-22
 * 
 */
public class BrowserFragment extends BaseFragment {

	protected BrowserView browserView;
	
	@Override
	public RelativeLayout onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		RelativeLayout rootView = super.onCreateView(inflater, container, savedInstanceState);
		
		browserView = new BrowserView(getActivity());
		
		addContentView(browserView);
		
		return rootView;
	}
	
	public final void loadUrl(String url) {
		if (browserView != null) {
			browserView.loadUrl(url);
		}
	}
	
	public final void loadJavaScript(String js) {
		if (browserView != null) {
			browserView.loadJavaScript(js);
		}
	}
	
	public final void addJavascriptInterface(String name, JavaCallback javaCallback) {
		if (browserView != null) {
			browserView.addJavascriptInterface(javaCallback, name);
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			loadJavaScript("finish()");
		}
		return false;
	}
}
