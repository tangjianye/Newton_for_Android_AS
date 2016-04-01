package com.fpliu.newton.business.account.login;

/**
 * 登录是异步过程，登录完成的回调
 * 
 * @author 792793182@qq.com 2015-03-03
 *
 */
interface LoginCallback {

	/**
	 * 登录成功的回调
	 * @param loginResult  登录结果
	 */
	void onLoginSuccess(LoginResult loginResult);
	
	/**
	 * 登录失败的回调
	 * @param loginResult  登录结果
	 */
	void onLoginFail(LoginResult loginResult);
}
