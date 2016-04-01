package com.fpliu.newton.framework.browser;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.ConsoleMessage;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.fpliu.newton.base.DebugLog;
import com.fpliu.newton.framework.API;

/**
 * 浏览器视图
 * 
 * @author 792793182@qq.com 2015-04-16
 * 
 */
public class BrowserView extends WebView {

	private static final String TAG = BrowserView.class.getSimpleName();
	
	/** 是否设置过的一个标志 */
	private static boolean isSet;
	
	public BrowserView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public BrowserView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public BrowserView(Context context) {
		super(context);
		init(context);
	}
	
	private final void init(Context context) {
		if (!isSet) {
			//4.4以后使用Chrome远程调试
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
				if (0 != (context.getApplicationInfo().flags &= ApplicationInfo.FLAG_DEBUGGABLE)) {
					WebView.setWebContentsDebuggingEnabled(true);
				}
			}
		}
		
		// 启用JavaScript
		getSettings().setJavaScriptEnabled(true);
		
		setWebViewClient(new MyWebViewClient());
		
		setWebChromeClient(new MyWebChromeClient());
		
		addJavascriptInterface(API.getInstance(), "api");
	}
	
	// 监听
	private static final class MyWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			DebugLog.d(TAG, "shouldOverrideUrlLoading() url = " + url);
			return super.shouldOverrideUrlLoading(view, url);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			DebugLog.d(TAG, "onPageFinished() url = " + url);
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
			DebugLog.d(TAG, "onPageStarted() url = " + url);
		}

		@Override
		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
			super.onReceivedError(view, errorCode, description, failingUrl);
			DebugLog.d(TAG, "onReceivedError() errorCode = " + errorCode + ", description = " + description + ", failingUrl = " + failingUrl);
		}
	}
	
	private static final class MyWebChromeClient extends WebChromeClient {
		@Override
		public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
			
			return super.onJsAlert(view, url, message, result);
		}
		
		@Override
		public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
			
			return super.onJsConfirm(view, url, message, result);
		}
		
		@Override
		public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
			if (url.startsWith("newton:")) {
				
			}
			//此函数没有什么实际用途，可以拿来别用
			return super.onJsPrompt(view, url, message, defaultValue, result);
		}
		
		@Override
		public void onConsoleMessage(String message, int lineNumber, String sourceID) {
			super.onConsoleMessage(message, lineNumber, sourceID);
			DebugLog.d(TAG, "message = " + message + ", lineNumber = " + lineNumber + ", sourceID = " + sourceID);
		}
		
		@Override
		public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
			return super.onConsoleMessage(consoleMessage);
		}
	}
	
	/**
	 * 加载JavaScript函数
	 * @param js
	 */
	public final void loadJavaScript(String js) {
		loadUrl("javascript:" + js);
	}
}
