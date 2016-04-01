package com.fpliu.newton.business.account.login;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.fpliu.newton.MyApp;
import com.fpliu.newton.R;
import com.fpliu.newton.base.DebugLog;
import com.fpliu.newton.base.ThreadPoolManager;
import com.fpliu.newton.framework.net.HttpClientRequest;
import com.fpliu.newton.framework.net.RequestCallback;
import com.fpliu.newton.framework.net.RequestStatus;
import com.fpliu.newton.framework.ui.toast.CustomToast;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;

/**
 * 微信登录
 * 
 * @author 792793182@qq.com 2015-03-03
 *
 */
final class WeixinLogin implements ILogin {

	private static final String TAG = WeixinLogin.class.getSimpleName();
	
	private LoginResult loginResult = new LoginResult();
	
	private boolean isLogined;
	
	private LoginCallback callback;
	
	@Override
	public void login(final Activity activity, Bundle pararms, final LoginCallback callback) {
		DebugLog.d(TAG, "login()");
		
		this.callback = callback;
		
		IWXAPI wxapi = MyApp.getApp().getWXAPI();
		if (!wxapi.isWXAppInstalled()) {
            //提醒用户没有安装微信
			String uninstalled = activity.getResources().getString(R.string.WeixinLogin_uninstalled);
			CustomToast.makeText(activity,uninstalled, 2000).show();
            return;
        }
		
		 SendAuth.Req req = new SendAuth.Req();
	     req.scope = "snsapi_userinfo";
	     req.state = "wechat_sdk_demo";
	     wxapi.sendReq(req);
	}

	@Override
	public void logout(Context context) {
		DebugLog.d(TAG, "logout()");
		
		isLogined = false;
	}

	@Override
	public boolean isLogin() {
		return isLogined;
	}
	
	@Override
	public LoginResult getLoginResult() {
		return loginResult;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		DebugLog.d(TAG, "onActivityResult()");
		
		final SendAuth.Resp resp = new SendAuth.Resp(intent.getExtras());
		
		//用户同意
        if (resp.errCode == BaseResp.ErrCode.ERR_OK) {
        	ThreadPoolManager.EXECUTOR.execute(new Runnable() {
				
				@Override
				public void run() {
					//获取accessToken
		        	String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + MyApp.getApp().b() + "&secret=" + MyApp.getApp().c() + "&code=" + resp.code + "&grant_type=authorization_code";
		        	HttpClientRequest.get(url, null, RequestAccessTokenResult.class, new RequestCallback<RequestAccessTokenResult>() {

						@Override
						public void callback(final RequestAccessTokenResult requestAccessTokenResult, RequestStatus status) {
							if (status.getHttpStatusCode() == 200) {
				        		//获取用户信息
					        	String url = "https://api.weixin.qq.com/sns/userinfo?access_token=" + requestAccessTokenResult.access_token + "&openid=" + requestAccessTokenResult.openid;
					        	HttpClientRequest.get(url, null, WeiXinUserInfo.class, new RequestCallback<WeiXinUserInfo>() {
									
									@Override
									public void callback(WeiXinUserInfo weiXinUserInfo, RequestStatus status) {
										if (status.getHttpStatusCode() == 200) {
											RequestThirdPartLogin.requestThirdPartLogin(3, weiXinUserInfo.openid, requestAccessTokenResult.access_token, weiXinUserInfo.sex == 1 ? "男" : "女", weiXinUserInfo.nickname, weiXinUserInfo.headimgurl, new RequestCallback<LoginResult>() {
												
												@Override
												public void callback(LoginResult loginResult, RequestStatus status) {
													if (status.getHttpStatusCode() == 200) {
														isLogined = true;
														WeixinLogin.this.loginResult = loginResult;
														
														if (callback != null) {
															loginResult.setLoginType(LoginType.WeiXin);
															callback.onLoginSuccess(loginResult);
														}
													} else {
														if (callback != null) {
															loginResult.setLoginType(LoginType.WeiXin);
															callback.onLoginFail(loginResult);
														}
													}
												}
											});
							        	} else {
							        		if (callback != null) {
												loginResult.setLoginType(LoginType.WeiXin);
												callback.onLoginFail(loginResult);
											}
										}
						        	}
								});
							}
						}
					});
				}
			});
        }
	}
	
	/**
	 * 获取accessToken
	 *
	 */
	private static final class RequestAccessTokenResult {
		
		private String access_token = "";
		private long expires_in;
		private String refresh_token = "";
		private String openid = "";
		private String scope = "";
		private String unionid = "";

		@Override
		public String toString() {
			return "RequestAccessTokenResult [access_token=" + access_token
					+ ", expires_in=" + expires_in + ", refresh_token="
					+ refresh_token + ", openid=" + openid + ", scope=" + scope
					+ ", unionid=" + unionid + ", " + super.toString();
		}
	}
	
	private static final class WeiXinUserInfo {

		//普通用户的标识，对当前开发者帐号唯一
		private String openid;
		
		//普通用户昵称
		private String nickname;
		
		//普通用户性别，1为男性，2为女性
		private int sex;
		
		//普通用户个人资料填写的省份
		private String province;
		
		//普通用户个人资料填写的城市
		private String city;
		
		//国家，如中国为CN
		private String country;
		
		//用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空
		private String headimgurl;
		
		//用户特权信息，json数组，如微信沃卡用户为（chinaunicom）
		private String privilege;
		
		//用户统一标识。针对一个微信开放平台帐号下的应用，同一用户的unionid是唯一的。
		private String unionid;

		@Override
		public String toString() {
			return "WeiXinUserInfo [openid=" + openid + ", nickname="
					+ nickname + ", sex=" + sex + ", province=" + province
					+ ", city=" + city + ", country=" + country
					+ ", headimgurl=" + headimgurl + ", privilege=" + privilege
					+ ", unionid=" + unionid + ", " + super.toString();
		}
	}
}