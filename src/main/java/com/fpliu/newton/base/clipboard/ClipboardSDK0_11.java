package com.fpliu.newton.base.clipboard;

import android.content.Context;
import android.text.ClipboardManager;

/**
 * Android3.0以下的剪贴板
 * 
 * @author 792793182@qq.com 2015-03-18
 */
@SuppressWarnings("deprecation")
class ClipboardSDK0_11 implements IClipboardManager {
	
	private ClipboardManager mClipboardManager;
	
	ClipboardSDK0_11(Context context) {
		mClipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
	}
	
	@Override
	public void copy(CharSequence text) {
		mClipboardManager.setText(text);
	}

	@Override
	public CharSequence paste() {
		return mClipboardManager.getText();
	}
}
