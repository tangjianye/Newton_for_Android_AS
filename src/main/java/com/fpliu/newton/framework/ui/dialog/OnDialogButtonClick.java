package com.fpliu.newton.framework.ui.dialog;

/**
 * 弹出框按钮点击事件处理
 * 
 * @author 792793182@qq.com 2015-06-12
 *
 */
public interface OnDialogButtonClick {
	
	/**
	 * 
	 * @param dialog  弹出框
	 * @param text    按钮的文字
	 */
	void onClick(CustomDialog dialog, String text);
}
