package com.fpliu.newton.business.account.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * 登录接口
 * 
 * @author 792793182@qq.com 2015-03-03
 *
 */
public interface ILogin {

	/** 登录 */
	void login(Activity activity, Bundle pararms, LoginCallback callback);
	
	/** 退出 */
	void logout(Context context);
	
	/** 是否已经登录了 */
	boolean isLogin();
	
	/** 获取登录结果 */
	LoginResult getLoginResult();
	
	/** 第三方获用于回调数据的 */
	void onActivityResult(int requestCode, int resultCode, Intent data);
}
